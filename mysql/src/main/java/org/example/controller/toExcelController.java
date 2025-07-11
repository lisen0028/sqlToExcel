package org.example.controller;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.ds.simple.SimpleDataSource;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import lombok.RequiredArgsConstructor;
import org.example.entity.exportRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class toExcelController {

    @PostMapping("/export")
    public ResponseEntity<byte[]> export(@RequestBody exportRequest request) {
        String url = "jdbc:mysql://" + request.getHost() + "/" + request.getDatabase()
                + "?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
        SimpleDataSource ds = new SimpleDataSource(url, request.getUser(), request.getPassword());
        try (Connection conn = ds.getConnection();
             ExcelWriter writer = ExcelUtil.getWriter(true)) {

            Db db = Db.use(ds);

            List<String> sqls = request.getSqls();
            List<String> sheets = request.getSheetNames();

            for (int i = 0; i < sqls.size(); i++) {
                String sql = sqls.get(i);
                String sheet = sheets.get(i);
                List<Entity> result = db.query(sql);
                writer.setSheet(sheet);
                writer.write(result, true);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            writer.flush(out, true);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("结果.xlsx", StandardCharsets.UTF_8)
                    .build());
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();  // 生产环境建议换成日志
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
