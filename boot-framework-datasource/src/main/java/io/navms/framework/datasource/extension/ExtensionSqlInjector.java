package io.navms.framework.datasource.extension;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.AlwaysUpdateSomeColumnById;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import org.apache.ibatis.session.Configuration;

import java.util.List;

/**
 * 扩展 SQl 注入器
 *
 * @author navms
 */
public class ExtensionSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Configuration configuration, Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> existsMethods = super.getMethodList(configuration, mapperClass, tableInfo);
        existsMethods.add(new InsertBatchSomeColumn(t -> t.getFieldFill() != FieldFill.UPDATE));
        existsMethods.add(new AlwaysUpdateSomeColumnById(t -> t.getFieldFill() != FieldFill.INSERT));
        return existsMethods;
    }

}
