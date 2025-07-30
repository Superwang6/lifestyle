package cn.fudges.server.generator;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.model.ClassAnnotationAttributes;

import java.sql.Types;

/**
 * @author 王平远
 * @since 2025/5/23
 */

public class CodeGenerator {

    public static final String TABLE_NAME = "file_upload";
    public static final String AUTHOR = "wpy";


    public static final String OUTPUT_DIR = "gcode";
    public static final String PARENT_PACKAGE = "cn.fudges.server";
    public static final String URL = "jdbc:mysql://local.fudges.cn:13307/lifestyle?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "601202";

    public static void main(String[] args) {
        FastAutoGenerator.create(URL, USERNAME, PASSWORD)
                .globalConfig(builder -> {
                    builder.author(AUTHOR) // 设置作者
                            .outputDir(OUTPUT_DIR);
                })
                .packageConfig(builder ->
                        builder.parent(PARENT_PACKAGE) // 设置父包名
                )
                .dataSourceConfig(builder ->
                        builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                            if (typeCode == Types.SMALLINT) {
                                // 自定义类型转换
                                return DbColumnType.INTEGER;
                            }
                            return typeRegistry.getColumnType(metaInfo);
                        })
                )
                .strategyConfig(builder ->
                        builder.addInclude(TABLE_NAME) // 设置需要生成的表名
                                // entity
                                .entityBuilder()
                                .enableLombok(new ClassAnnotationAttributes("@Data", "lombok.Data"))
                                .enableFileOverride()
                                .enableRemoveIsPrefix()
                                .enableTableFieldAnnotation()
                                .enableFileOverride()
                                // handler
                                .controllerBuilder()
                                .enableRestStyle()
                                .enableFileOverride()
                                // mapper
                                .mapperBuilder()
                                .enableBaseResultMap()
                                .enableBaseColumnList()
                                .enableFileOverride()
                                // service
                                .serviceBuilder()
                                .formatServiceFileName("%sService")
                                .enableFileOverride()
                )
                .injectionConfig(injectionConfig ->
                        injectionConfig
                                .customFile(
                                        new CustomFile.Builder()
                                                .fileName("Response.java")
                                                .templatePath("templates/entityResponse.java.vm")
                                                .packageName("response")
                                                .enableFileOverride()
                                                .build()
                                )
                                .customFile(
                                        new CustomFile.Builder()
                                                .fileName("Request.java")
                                                .templatePath("templates/entityRequest.java.vm")
                                                .packageName("request")
                                                .enableFileOverride()
                                                .build()
                                )
                                .customMap(
                                        Dict.create()
                                                .set("package.Request", PARENT_PACKAGE + ".request")
                                                .set("package.Response", PARENT_PACKAGE + ".response")
                                )
                )
                .execute();
    }
}
