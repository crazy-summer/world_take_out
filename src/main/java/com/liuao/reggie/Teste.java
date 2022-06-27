package com.liuao.reggie;

import org.springframework.util.DigestUtils;

public class Teste {
    public static void main(String[] args) {
        String a = "asdfasdf";
        byte[] bytes = DigestUtils.md5Digest(a.getBytes());
        System.out.println(bytes.toString());
    }
}
