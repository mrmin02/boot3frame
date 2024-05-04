package com.custom.boot3Cms.application.common.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

/**
 * 이미지 유틸
 *
 * @author SEKOREA
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2017-09-12 / 최재민	 / 최초 생성
 * </pre>
 * @since 2017-09-12
 */
public class ImageUtil {

    /**
     * 이미지 가로, 세로, 경로를 받아 리사이징하여 저장한다.
     *
     * @param maxWidth
     * @param maxHeight
     * @param fullPath
     * @param fileName
     * @throws IOException
     */
    public static void fn_resizeImg(int maxWidth, int maxHeight, String fullPath, String fileName) throws IOException {

        BufferedImage originalImg = ImageIO.read(new File(fullPath + fileName));

        int[] centerPoint = {originalImg.getWidth() / 2, originalImg.getHeight() / 2};
        int cropWidth = originalImg.getWidth();
        int cropHeight = originalImg.getHeight();

        if(originalImg.getWidth() > maxWidth) {
            cropWidth = maxWidth;
        }
        if(originalImg.getHeight() > maxHeight){
            cropHeight = maxHeight;
        }

        // 요구하는 사이즈보다 이미지가 클 경우에만 크롭..
        if(maxWidth < originalImg.getWidth() && maxHeight < originalImg.getHeight()){

            BufferedImage targetImage = new BufferedImage(cropWidth, cropHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = targetImage.createGraphics();
            graphics2D.setBackground(Color.WHITE);
            graphics2D.setPaint(Color.WHITE);
            graphics2D.fillRect(0, 0, cropWidth, cropHeight);
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            graphics2D.drawImage(originalImg, 0, 0, cropWidth, cropHeight, centerPoint[0] - (int) (cropWidth / 2), centerPoint[1] - (int) (cropHeight / 2), centerPoint[0] + (int) (cropWidth / 2), centerPoint[1] + (int) (cropHeight / 2), null);

            String[] fileNameArr = fileName.split("\\.");
            File newImgFile = new File(fullPath + fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(newImgFile);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(targetImage, fileNameArr[fileNameArr.length - 1], output);

            fileOutputStream.write(output.toByteArray());
        }
    }

    public static void fn_resizeImageRatio(int maxHeight, String fullPath, String fileName) throws IOException {
        ImageIO.setUseCache(false);
        BufferedImage originalImg = ImageIO.read(new File(fullPath + fileName));

        int originWidth = originalImg.getWidth();
        int originHeight = originalImg.getHeight();

        if (originHeight > maxHeight) {
            // 기존 이미지 비율을 유지하여 가로 길이 설정
            int maxWidth = (originWidth * maxHeight) / originHeight;
            // 이미지 품질 설정
            // Image.SCALE_DEFAULT : 기본 이미지 스케일링 알고리즘 사용
            // Image.SCALE_FAST : 이미지 부드러움보다 속도 우선
            // Image.SCALE_REPLICATE : ReplicateScaleFilter 클래스로 구체화 된 이미지 크기 조절 알고리즘
            // Image.SCALE_SMOOTH : 속도보다 이미지 부드러움을 우선
            // Image.SCALE_AREA_AVERAGING : 평균 알고리즘 사용
            Image resizeImage = originalImg.getScaledInstance(maxWidth, maxHeight, Image.SCALE_SMOOTH);
            BufferedImage newImage = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = newImage.getGraphics();
            graphics.drawImage(resizeImage, 0, 0, null);
            graphics.dispose();

            // 이미지 저장
            String[] fileNameArr = fileName.split("\\.");
            File newImgFile = new File(fullPath + fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(newImgFile);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(newImage, fileNameArr[fileNameArr.length - 1], output);

            fileOutputStream.write(output.toByteArray());
        }
    }

    public static void fn_resizeImageRatioOfWidthGijun(int maxWidth, String fullPath, String fileName) throws IOException {
        ImageIO.setUseCache(false);
        BufferedImage originalImg = ImageIO.read(new File(fullPath + fileName));

        int originWidth = originalImg.getWidth();
        int originHeight = originalImg.getHeight();

        if (originWidth > maxWidth) {
            // 기존 이미지 비율을 유지하여 가로 길이 설정
            int maxHeight = (originHeight * maxWidth) / originWidth;
            // 이미지 품질 설정
            // Image.SCALE_DEFAULT : 기본 이미지 스케일링 알고리즘 사용
            // Image.SCALE_FAST : 이미지 부드러움보다 속도 우선
            // Image.SCALE_REPLICATE : ReplicateScaleFilter 클래스로 구체화 된 이미지 크기 조절 알고리즘
            // Image.SCALE_SMOOTH : 속도보다 이미지 부드러움을 우선
            // Image.SCALE_AREA_AVERAGING : 평균 알고리즘 사용
            Image resizeImage = originalImg.getScaledInstance(maxWidth, maxHeight, Image.SCALE_SMOOTH);
            BufferedImage newImage = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = newImage.getGraphics();
            graphics.drawImage(resizeImage, 0, 0, null);
            graphics.dispose();

            // 이미지 저장
            String[] fileNameArr = fileName.split("\\.");
            File newImgFile = new File(fullPath + fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(newImgFile);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(newImage, fileNameArr[fileNameArr.length - 1], output);

            fileOutputStream.write(output.toByteArray());
        }
    }

    public static void fn_CopyResizeImageRatioOfWidthGijun(int maxWidth, String fullPath, String fileName, String thumbFileName) throws IOException {
        ImageIO.setUseCache(false); //-Djava.io.tmpdir=/path/to/tmpdir
        BufferedImage originalImg = ImageIO.read(new File(fullPath + fileName));

        int originWidth = originalImg.getWidth();
        int originHeight = originalImg.getHeight();

        if (originWidth > maxWidth) {
            // 기존 이미지 비율을 유지하여 가로 길이 설정
            int maxHeight = (originHeight * maxWidth) / originWidth;
            // 이미지 품질 설정
            // Image.SCALE_DEFAULT : 기본 이미지 스케일링 알고리즘 사용
            // Image.SCALE_FAST : 이미지 부드러움보다 속도 우선
            // Image.SCALE_REPLICATE : ReplicateScaleFilter 클래스로 구체화 된 이미지 크기 조절 알고리즘
            // Image.SCALE_SMOOTH : 속도보다 이미지 부드러움을 우선
            // Image.SCALE_AREA_AVERAGING : 평균 알고리즘 사용
            Image resizeImage = originalImg.getScaledInstance(maxWidth, maxHeight, Image.SCALE_SMOOTH);
            BufferedImage newImage = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = newImage.getGraphics();
            graphics.drawImage(resizeImage, 0, 0, null);
            graphics.dispose();

            // 이미지 저장
            String[] fileNameArr = thumbFileName.split("\\.");
            File newImgFile = new File(fullPath + thumbFileName);
            FileOutputStream fileOutputStream = new FileOutputStream(newImgFile);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(newImage, fileNameArr[fileNameArr.length - 1], output);

            fileOutputStream.write(output.toByteArray());
        } else {

            int maxHeight = (originHeight * maxWidth) / originWidth;

            Image resizeImage = originalImg.getScaledInstance(maxWidth, maxHeight, Image.SCALE_SMOOTH);
            BufferedImage newImage = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = newImage.getGraphics();
            graphics.drawImage(resizeImage, 0, 0, null);
            graphics.dispose();

            // 이미지 저장
            String[] fileNameArr = thumbFileName.split("\\.");
            File newImgFile = new File(fullPath + thumbFileName);
            FileOutputStream fileOutputStream = new FileOutputStream(newImgFile);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(newImage, fileNameArr[fileNameArr.length - 1], output);

            fileOutputStream.write(output.toByteArray());

        }
    }



    /**
     * 원본 이미지를 활용하여 리사이징된 이미지를 생성한다.
     *
     * @author mitw.tistory.com
     * @see <pre>
     *  Modification Information
     *
     * 	수정일      / 수정자    / 수정내용
     * 	------------------------------------------
     * 	2021-09-12 / 김기식	 / 최초 생성
     * </pre>
     *
     * @since 2021-09-12
     * @param maxPixel String : 최대사이즈
     * @param criteria String : 리사이징 시 최대사이즈의 기준 (가로:width,세로:그 외)
     * @param orgFullPath String : 원본 이미지 파일 경로
     * @param orgFileName String : 원본 이미지 파일명
     * @param resizeFullPath String : 리사이징된 이미지 파일 저장 경로
     * @param resizeFileName String : 리사이징된 이미지 파일명
     * @param resizeType String : 리사이징 타입 (crop, normal)
     * @exception IOException : 원본 이미지에 대한 문제가 발생한 경우
     * */
    public static void copyResizeImageRatioCriteria(int maxPixel, String criteria, String orgFullPath, String orgFileName, String resizeFullPath, String resizeFileName, String resizeType) throws IOException {
        //이미지 캐시를 사용하지 않는다.
        //사용을 원하면 tmp경로를 완벽하게 구성하거나, VM option에 -Djava.io.tmpdir=/path/to/tmpdir를 추가한다.
        ImageIO.setUseCache(false);
        //원본 파일을 불러온다.
        BufferedImage originalImg = ImageIO.read(new File(orgFullPath + File.separator + orgFileName));
        //원본 이미지의 가로와 세로 사이즈를 가져온다.
        int originWidth = originalImg.getWidth(), width;
        int originHeight = originalImg.getHeight(), height;

        boolean isWidth = criteria.toLowerCase(Locale.ROOT).equals("width");

        //리사이징할 이미지의 기준을 정한다. 가로 기준인지 세로 기준인지
        //기준의 최고 픽셀보다 원본이 큰지 작은지
        if(isWidth) {
            if (originWidth > maxPixel) {
                height = (originHeight * maxPixel) / originWidth;
                width = maxPixel;
            } else {
                height = originHeight;
                width = originWidth;
            }
        } else {
            if (originHeight > maxPixel) {
                height = maxPixel;
                width = (originWidth * maxPixel) / originHeight;
            } else {
                height = originHeight;
                width = originWidth;
            }
        }
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //중앙을 중심으로 CROP 형태로 저장할 지, 일반형태로 저장할 지
        if(resizeType.toLowerCase(Locale.ROOT).equals("crop")) {
            int[] centerPoint = {originalImg.getWidth() / 2, originalImg.getHeight() / 2};
            Graphics2D graphics2D = newImage.createGraphics();
            graphics2D.setBackground(Color.WHITE);
            graphics2D.setPaint(Color.WHITE);
            graphics2D.fillRect(0, 0, width, height);
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.drawImage(originalImg, 0, 0, width, height,
                    centerPoint[0] - (int) (width / 2), centerPoint[1] - (int) (height / 2),
                    centerPoint[0] + (int) (width / 2), centerPoint[1] + (int) (height / 2), null);
        } else {
            Image resizeImage = originalImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            Graphics graphics = newImage.getGraphics();
            graphics.drawImage(resizeImage, 0, 0, null);
            graphics.dispose();
        }
        // 이미지 저장
        String[] fileNameArr = resizeFileName.split("\\.");
        File newImgFile = new File(resizeFullPath + resizeFileName);
        FileOutputStream fileOutputStream = new FileOutputStream(newImgFile);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(newImage, fileNameArr[fileNameArr.length - 1], output);
        fileOutputStream.write(output.toByteArray());
    }


}
