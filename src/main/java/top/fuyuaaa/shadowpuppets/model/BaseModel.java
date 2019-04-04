package top.fuyuaaa.shadowpuppets.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: fuyuaaa
 * @creat: 2019-03-27 21:38
 */
@Data
public class BaseModel implements Serializable {
    private static final long serialVersionUID = -558729523380287416L;
    private Date dateCreate;
    private Date dateUpdate;
    private Date dateDelete;
}
