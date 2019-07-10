package com.otaliastudios.cameraview;

import android.graphics.Bitmap;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Observable;

import java.io.File;

/**
 * A decorator class that adds the functionality to produce a bitmap as an observable.
 * This functionality is desired when the result is used in a chain of rx computations
 * and we do not want the bitmap to be posted onto the ui thread, but be able to decide
 * on which thread we want it.
 */
@SuppressWarnings("WeakerAccess")
public class RxPictureResult extends PictureResult {
    private final PictureResult pictureResult;

    public RxPictureResult(PictureResult pictureResult) {
        this.pictureResult = pictureResult;
    }

    public Observable<Bitmap> toBitmap(int maxWidth, int maxHeight) {
        return Observable.fromCallable(() -> {
            System.out.println("Bitmap on " + Thread.currentThread().getName());
            Bitmap result = CameraUtils.decodeBitmap(pictureResult.getData(), maxWidth, maxHeight);
            if (result == null) {
                throw new IllegalStateException("Picture cannot be converted to bitmap. Most probable Out Of Memory");
            }
            return result;
        });
    }

    public Observable<Bitmap> toBitmap() {
        return toBitmap(-1, -1);
    }

    @Override
    public boolean isSnapshot() {
        return pictureResult.isSnapshot();
    }

    @Nullable
    @Override
    public Location getLocation() {
        return pictureResult.getLocation();
    }

    @Override
    public int getRotation() {
        return pictureResult.getRotation();
    }

    @NonNull
    @Override
    public Size getSize() {
        return pictureResult.getSize();
    }

    @NonNull
    @Override
    public Facing getFacing() {
        return pictureResult.getFacing();
    }

    @NonNull
    @Override
    public byte[] getData() {
        return pictureResult.getData();
    }

    @Override
    public int getFormat() {
        return pictureResult.getFormat();
    }

    @Override
    public void toBitmap(int maxWidth, int maxHeight, @NonNull BitmapCallback callback) {
        pictureResult.toBitmap(maxWidth, maxHeight, callback);
    }

    @Override
    public void toBitmap(@NonNull BitmapCallback callback) {
        pictureResult.toBitmap(callback);
    }

    @Override
    public void toFile(@NonNull File file, @NonNull FileCallback callback) {
        pictureResult.toFile(file, callback);
    }
}
