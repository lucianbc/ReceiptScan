import cv2 as cv
import numpy as np
import pytesseract as tes


text = get_text_from_image("resizedReceipt.jpg")
print(text)


def get_text_from_image(imageName):
    img = preprocess(imageName)
    result = tes.image_to_string(img)
    return result


def preprocess(image_name):
    image = cv.imread(image_name)
    gray = cv.cvtColor(image, cv.COLOR_BGR2GRAY)
    receiptBox = find_receipt_box(gray)
    M, w, h = perspective_transform(receiptBox)
    receiptImg = apply_perspective_correction(gray, M, w, h)
    receiptImg = cv.adaptiveThreshold(receiptImg, 255, cv.ADAPTIVE_THRESH_GAUSSIAN_C, cv.THRESH_BINARY, 71, 10)
    return receiptImg


def find_receipt_box(image):
    """
    Finds a contour around the receipt in the given image.
    Returns the bounding box and the binary image
    """
    # gray = cv.cvtColor(image, cv.COLOR_BGR2GRAY)
    gray = cv.medianBlur(image, 15, 0)
    _, thresh = cv.threshold(gray, 255, 125, cv.THRESH_BINARY | cv.THRESH_OTSU)
    k = np.ones((25, 25))
    thresh = cv.erode(thresh, k, iterations=1)
    thresh = cv.dilate(thresh, k, iterations=1)
    contours = cv.findContours(thresh, cv.RETR_LIST, cv.CHAIN_APPROX_SIMPLE)
    contours = sorted(contours[0], key=cv.contourArea, reverse=True)
    contour = contours[0]
    rect = cv.minAreaRect(contour)
    box = cv.boxPoints(rect)
    box = np.int0(box)
    return box


def perspective_transform(contour):
    """Produces the transformation matrix and the new size for perspective correction"""
    ord_rect = np.float32(order_rect(contour))
    (tl, tr, br, bl) = ord_rect

    dist_top = np.linalg.norm(tl - tr)
    dist_btm = np.linalg.norm(bl - br)
    width = max(dist_btm, dist_top)

    dist_left = np.linalg.norm(tl - tr)
    dist_right = np.linalg.norm(tr - br)
    height = max(dist_left, dist_right)

    dest_corners = np.array([
        [0, 0],
        [width - 1, 0],
        [width - 1, height - 1],
        [0, height - 1]
    ], dtype=ord_rect.dtype)

    M = cv.getPerspectiveTransform(ord_rect, dest_corners)
    return M, width, height


def order_rect(pts):
    """
    orders a rectangle in the order top-left, top-right,
    bottom-right, bottom-left
    """
    new = np.zeros((4, 2), dtype="int64")
    s = pts.sum(axis=1)
    new[0] = pts[np.argmin(s)]
    new[2] = pts[np.argmax(s)]

    diff = np.diff(pts, axis=1)
    new[1] = pts[np.argmin(diff)]
    new[3] = pts[np.argmax(diff)]

    return new


def apply_perspective_correction(image, M, width, height):
    """Crops the contour and applies perspective correction"""
    warped = cv.warpPerspective(image, M, (width, height))
    return warped
