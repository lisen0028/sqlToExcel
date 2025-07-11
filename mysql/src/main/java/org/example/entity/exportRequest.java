package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor // Jackson 需要无参构造函数
public class exportRequest {
    private String host;
    private String database;
    private String user;
    private String password;
    private List<String> sqls;
    private List<String> sheetNames;
}
