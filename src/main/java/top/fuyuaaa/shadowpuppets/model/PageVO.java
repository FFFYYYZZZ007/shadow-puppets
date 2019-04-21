package top.fuyuaaa.shadowpuppets.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-16 22:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageVO<T> implements Serializable {
    private static final long serialVersionUID = 3904992153837749694L;
    private Integer pageNum;
    private Integer pageSize;
    private Integer total;
    private List<T> list;
}
