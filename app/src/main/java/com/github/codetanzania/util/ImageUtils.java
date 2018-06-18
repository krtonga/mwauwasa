package com.github.codetanzania.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.util.ArrayMap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageUtils {

    // used by logcat
    private static final String TAG = "ImageUtils";

    public static final int DEFAULT_JPEG_COMPRESSION_QUALITY = 70;
    public static final int MAX_COMPRESSION_QUALITY = 100;
    public static final String TMP_PHOTO_DIR = "tmp_photos";
    public static final String IMAGE_TYPE_TOKEN_SEPARATOR = ":";

    private static final Pattern[] SUPPORTED_IMAGE_PATTERNS = {
            Pattern.compile("(?:image/jpeg|image/jpg)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("image/png", Pattern.CASE_INSENSITIVE)
    };

    // -- must match the SUPPORTED
    private static final Bitmap.CompressFormat[] SUPPORTED_IMAGE_COMPRESS_FORMAT = {
            Bitmap.CompressFormat.JPEG,
            Bitmap.CompressFormat.PNG
    };

    public static final int DEFAULT_MAX_BITMAP_WIDTH  = 480;
    public static final int DEFAULT_MAX_BITMAP_HEIGHT = 320;

    /**
     * Gets the real path from file
     * @param context
     * @param contentUri
     * @return path
     */
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return getPathForV19AndUp(context, contentUri);
        } else {
            return getPathForPreV19(context, contentUri);
        }
    }

    /**
     * Handles pre V19 uri's
     * @param context
     * @param contentUri
     * @return
     */
    public static String getPathForPreV19(Context context, Uri contentUri) {
        String res = null;

        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();

        return res;
    }

    /**
     * Handles V19 and up uri's
     * @param context
     * @param contentUri
     * @return path
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPathForV19AndUp(Context context, Uri contentUri) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            result = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public static String encodeToBase64String(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

//    public static String encodeToBase64String(Context ctx, Uri photoUri, Bitmap.CompressFormat compressFormat, int quality) {
//        Bitmap bitmap = AttachmentUtils.getScaledBitmap(photoUri.getPath(), 500, 500);
//
//        if (bitmap != null) {
//            return encodeToBase64String(bitmap, compressFormat, quality);
//        }
//        return null;
//    }

    public static Bitmap decodeFromBase64String(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static File createImageFile(Context ctx) throws IOException {
        String timestamp = new SimpleDateFormat(
                "yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFile = "JPEG_" + timestamp + "_";
        // allow media scanner to index the captured issues
        File  storageDir = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFile, ".jpg", storageDir);
    }


    public static File getTemporaryAlbumStorageDir(Context ctx, String albumName) {
        // Get the directory for the user's public pictures directory.
        ContextWrapper cw = new ContextWrapper(ctx);
        File file = cw.getDir(albumName, Context.MODE_PRIVATE);

        // automatically delete when app exits
        file.deleteOnExit();

        // debug
        Log.d(TAG, file.getPath());

        return file;
    }

    private static Bitmap.CompressFormat getCompressionFormat(String contentType) {
        for (int i = 0; i < SUPPORTED_IMAGE_PATTERNS.length; ++i) {
            Matcher matcher = SUPPORTED_IMAGE_PATTERNS[i].matcher(contentType);
            if (matcher.find()) {
                return SUPPORTED_IMAGE_COMPRESS_FORMAT[i];
            }
        }
        return null;
    }

    public static Bitmap handleSamplingAndRotationBitmap(
            Context context, Uri selectedImage)
            throws IOException {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(
                options, DEFAULT_MAX_BITMAP_WIDTH, DEFAULT_MAX_BITMAP_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(context, img, selectedImage);
        return img;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    public static Bitmap rotateImageIfRequired(Context ctx, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = ctx.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    public static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public static ArrayMap<String, String> compressAndCacheBitmaps(
            File album, ArrayMap<String, Bitmap> files, int quality) throws IOException {

        FileOutputStream out = null;
        ArrayMap<String, String> uris = new ArrayMap<>();

        try {
            File file;
            for (int i = 0; i < files.size(); i++) {

                String[] parts = files.keyAt(i).split(IMAGE_TYPE_TOKEN_SEPARATOR);

                file = new File(album, parts[0]);
                // debug
                Log.d(TAG, file.getPath());
                out  = new FileOutputStream(file);
                Bitmap.CompressFormat compressFormat = getCompressionFormat(parts[1]);

                if (compressFormat == null) {
                    /*
                     * ignore the formats which android does not recognize
                     */
                    continue;
                }

                // store file
                files.valueAt(i).compress(compressFormat, quality, out);
                uris.put(parts[0], Uri.fromFile(file).toString());
            }

        } catch(IOException ioException) {
            ioException.printStackTrace();
            throw new IOException(
                    "Error Caching Photo(s).\nOriginal Exception was:\n" + ioException.getMessage());
        } catch(ArrayIndexOutOfBoundsException aio) {
            aio.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ioException) {
                    // ignore
                }
            }
        }

        // debug
        Log.d(TAG, String.format("%s", uris));

        return uris;
    }
}
