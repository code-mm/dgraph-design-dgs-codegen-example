
/**
 * Copyright © 2022 WXFGGZS. All Rights Reserved.
 */

package com.my.app;

import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 更新Dgraph Schema
 */
@SuppressWarnings("all")
public class UpdateDgraphSchema {
    // TODO 替换层自己的服务地址
//    private static final String baseUrl = "http://127.0.0.1:8080/";
    private static final String baseUrl = "";

    // TODO 如果使用 dgraph.io 替换成自己的密钥
    private static final String API_KEY ="";

    public static final MediaType mediaType = MediaType.Companion.parse("application/octet-stream");
    public static final MediaType APPLICATION_JSON = MediaType.Companion.parse("application/json; charset=utf-8");

    private static final OkHttpClient okhttp = new OkHttpClient();


    private static void updateSchema() throws Exception {
        System.out.println("更新schema-开始");
        FileInputStream fileInputStream =
                new FileInputStream("build/schema.txt");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = 0;
        byte[] b = new byte[1024];
        while ((len = fileInputStream.read(b, 0, b.length)) != -1) {
            baos.write(b, 0, len);
        }
        byte[] buffer = baos.toByteArray();
        final RequestBody requestBody = RequestBody.Companion.create(buffer, mediaType);
        Request request = new Request.Builder().url(baseUrl + "admin/schema")
                .addHeader("Authorization",API_KEY)
                .post(requestBody).build();
        Response response =okhttp.newCall(request).execute();
        System.out.println(response.code());
        if (response.code() == 200) {
            System.out.println(response.body().string());
        }
        System.out.println("更新schema-结束");
    }


    private static void downloadSchemaGraphql() throws Exception {
        System.out.println("下载schema-开始");
        String gqlSchema = SchemaKt.getGQLSchema();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", gqlSchema);
        RequestBody requestBody = RequestBody.Companion.create(jsonObject.toString(), APPLICATION_JSON);
        Request request = new Request.Builder().url(baseUrl + "admin")
                .addHeader("Authorization",API_KEY)
                .post(requestBody).build();
        Response response = okhttp.newCall(request).execute();
        System.out.println(response.code());
        if (response.code() == 200) {

            if (true) {
                String body = response.body().string();
//            body = body.replace("int", "");
//            body = body.replace("float", "");

                String generatedSchema = new JSONObject(body).getJSONObject("data").getJSONObject("getGQLSchema").getString("generatedSchema");
                File tmp = new File("tmp");
                if (!tmp.exists()) {
                    tmp.mkdir();
                }
                PrintStream ps = new PrintStream("tmp/schema0.graphqls");
                ps.println(generatedSchema);
                ps.close();
            }

            if (true) {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("tmp/schema0.graphqls")));
                String str = null;
                PrintStream ps = new PrintStream("tmp/schema.graphqls");
                while ((str = br.readLine()) != null) {
                    if (str.contains("\tint")
                            || str.contains("\tfloat")
                    ) {

                    } else {
                        ps.println(str);
                    }
                }
                ps.close();
            }
        }
        System.out.println("下载schema-结束");
    }


    public static List<String> paths = new ArrayList<>();

    public static void search(String path) {
        File file = new File(path);

        File[] files = file.listFiles();
        for (File it : files) {
            if (it.isDirectory()) {
                search(it.getPath());
            }
            if (it.isFile()) {
                paths.add(it.getPath());
            }
        }
    }

    private static void mergeSchema() throws Exception {
        System.out.println("合并schema-开始");
        PrintStream ps = new PrintStream("build/schema.txt");
        search("schema/dgraph");
        for (String path : paths) {
            String s = FileUtils.readToString(path.toString());
            ps.println(s);
        }
        ps.close();
        System.out.println("合并schema-结束");
    }

    public static void main(String[] args) throws Exception {
        mergeSchema();
        updateSchema();
        downloadSchemaGraphql();
    }
}