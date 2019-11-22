package com.qfzxr.phoneCode.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.Random;

/**
 * @Author zxr
 * @Date 2019/11/20 20:42
 * @Description TODO
 * @Version 1.0
 * @ProjectName
 **/
@RestController
public class UserController {

    @RequestMapping("/sendCode")
    public String sendCode(String phone){
        if (phone==null){
            return "error";
        }
        String verfyCode = genCode(4);

        Jedis jedis = new Jedis("192.168.228.135",6379);

        String phonekey = "phone_num" + phone;
        jedis.setex(phonekey,20,verfyCode);

        System.out.println(verfyCode);
        return "success";
    }


    private String genCode(int code_length){
        String code = "";
        for (int i = 0; i <code_length ; i++) {
            int num = new Random().nextInt(10);
            code += num;

        }
        return code;
    }

    @RequestMapping("/verifiCode")
    public String verifiCode(String phone,String verify_code) {
        if (verify_code == null) {
            return "error";
        }

        Jedis jedis = new Jedis("192.168.228.135",6379);
        String phonekey = jedis.get("phone_num" + phone);

        System.out.println(phonekey);

        if (verify_code.equals(phonekey)){
            return "succes";
        }
        jedis.close();
        return "error";
    }
}
