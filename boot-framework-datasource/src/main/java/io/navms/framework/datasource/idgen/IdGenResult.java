package io.navms.framework.datasource.idgen;

import lombok.Getter;

/**
 * ID 生成结果
 *
 * @author navms
 */
@Getter
public class IdGenResult {

    private Long id;
    private Status status;

    public static IdGenResult success(Long id) {
        IdGenResult result = new IdGenResult();
        result.id = id;
        result.status = Status.SUCCESS;
        return result;
    }

    public static IdGenResult failure(Status status) {
        IdGenResult result = new IdGenResult();
        result.status = status;
        return result;
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS && id != null;
    }
}
