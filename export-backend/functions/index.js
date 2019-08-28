const functions = require('firebase-functions');
const admin = require('firebase-admin');
const archiver = require('archiver');
const uuid = require('uuid/v4');
const path = require('path');

admin.initializeApp();

exports.exportProcessor = functions
    .firestore.document('/manifests/{manifestId}')
    .onCreate( event => {
        console.log("Function triggered!");

        const manifest = event.data();

        console.log(manifest);

        const zip = archiver('zip', { zlib: { level: 9 } });

        const bucket = admin.storage().bucket();

        // defining a promise for the output
        const outputPath = `/downloads/${uuid()}.zip`;
        const uploadOptions = {
            destination: outputPath,
            validation: 'md5',
            metadata: {
                contentType: 'application/zip'
            }
        };
        const outputBucket = bucket.file(outputPath).createWriteStream(uploadOptions);
        const sendZipPromise = new Promise((resolve, reject) => {
            outputBucket.on('finish', () => resolve(outputPath));
            outputBucket.on('error', err => reject(err));
        });
        zip.pipe(outputBucket);

        // Adds each file from a bucket to the target zip.
        // Each filename should contain the whole path.
        const addFilesToZipPromise = (filenames) => Promise.all(
            filenames.map(f => {
                const reader = bucket.file(f).createReadStream();

                return new Promise((resolve, reject) => {
                    reader.on('error', e => reject(e));
                    reader.on('end', resolve);
                    zip.append(reader, { name: path.basename(f) })
                })
            })
        ).then(r => {
            zip.finalize(); return r;
        });


        const accessLinkPromise = (fileName) => {
            const file = bucket.file(fileName);
            const expiration = new Date();
            expiration.setMonth(expiration.getMonth() + 1);
            const urlOptions = {
                action: 'read',
                expires: expiration
            };
            return file.getSignedUrl(urlOptions)
                .then(result => {
                    return {
                        url: result[0],
                        expires: expiration
                    }
                })
        };

        const filesPromise = bucket
            .getFiles({ prefix: manifest.id })
            .then(result => result[0].map(file => file.name));


        const notifyPromiseFactoryFunc = myManifest => payload => {
            return admin
                .messaging()
                .sendToDevice(myManifest.notificationToken, {
                    data: payload,
                    notification: {
                        title: 'Export Finished'
                    }
                })
        };

        const notifyPromiseFactory =
            manifest.notificationToken
                ? notifyPromiseFactoryFunc(manifest)
                : () => Promise.resolve();

        console.log("before read");

        const result = filesPromise
            .then(filenames => addFilesToZipPromise(filenames))
            .then(() => sendZipPromise)
            .then(result => {
                console.log(`Finished writing to ${result}`);
                return result;
            })
            .catch(err => {
                console.log("Error somewhere");
                console.log(err);
                throw err;
            })
            .then(result => accessLinkPromise(result))
            .then(result => {
                console.log("Finished making the file public.");
                console.log(result);
                return result;
            })
            .then( result => {
                console.log("Sending notification to device");
                return notifyPromiseFactory(result)
                    .then(() => {
                        console.log("Notification sent");
                        return result;
                    })
                    .catch((err) => {
                        console.log("Error sending", err)
                    })
            });

        console.log("after read");

        return result;
    });
