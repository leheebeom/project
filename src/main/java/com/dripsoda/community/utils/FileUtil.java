package com.dripsoda.community.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

//기본 값 설정
public final class FileUtil {
    //    public static byte[] getBytes(String path) {
//        FileInputStream fis = null;
//        ByteArrayOutputStream byteOs = null;
//        try {
//            //FileIS 생성
//            fis = new FileInputStream(path);
//            //byteArrayOS 생성
//            byteOs = new ByteArrayOutputStream();
//            //바이트 하나씩 가져올 변수
//            int readFile = 0;
//            //하나씩 꺼내서 읽는데 다 읽으면 -1을 리턴
//            while((readFile = fis.read()) != -1) {
//                //생성한 OS에 읽은 데이터 쓰기
//                byteOs.write(readFile);
//            }
//        }catch(IOException e){
//            System.out.println(e.toString());
//        }finally {
//            if(fis != null) {
//                try {fis.close();}catch(IOException e) {}
//            }
//        }
//        //OS에 쓴 데이터 byte[]로 리턴
//        return byteOs.toByteArray();
//    }
    private FileUtil() {
        // private constructor to prevent instantiation
    }

    public static byte[] getBytes(String path) {
        try (InputStream inputStream = FileUtil.class.getClassLoader().getResourceAsStream(path);
             ByteArrayOutputStream byteOs = new ByteArrayOutputStream()) {

            if (inputStream == null) {
                // 리소스를 찾을 수 없음
                System.out.println("Resource not found: " + path);
                return new byte[0];
            }

            int readFile;
            while ((readFile = inputStream.read()) != -1) {
                byteOs.write(readFile);
            }

            return byteOs.toByteArray();
        } catch (IOException e) {
            // 예외가 발생하면 로그에 기록
            e.printStackTrace();
        }

        // 예외 발생 시 또는 파일이 없을 경우 null이 아닌 빈 byte 배열 반환
        return new byte[0];
    }
}
