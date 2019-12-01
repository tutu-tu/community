package life.majiang.community.entity;

import life.majiang.community.model.Question;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {

    private List<QuestionDTO> questions;
    private boolean showPrevious;                       //是否显示上一页按钮
    private boolean showFirstPage;                      //是否显示第一页
    private boolean showNext;                           //是否显示下一页
    private boolean showEndPage;                        //是否显示尾页
    private Integer page;                               //当前页
    private List<Integer> pages = new ArrayList<>();    //当前页的数 1，2，3，4，5
    private Integer totalPage;                          //最后一页
    public void setPagination(Integer totalCount, Integer page, Integer size) {

        if(totalCount % size == 0){
            totalPage = totalCount/size;
        }else {
            totalPage = (totalCount/size)+1;
        }

        if(page <1 ){
            page =1;
        }
        if(page > totalPage){
            page = totalPage;
        }

        this.page = page;
        pages.add(page);
        for (int i = 1;i <= 3;i++){
            if(page -i >0){
                pages.add(0,page -i);
            }
            if(page +i <= totalPage){
                pages.add(page + i);
            }
        }
        //是否显示上一页
        if(page == 1){
            showPrevious = false;
        }else {
            showPrevious = true;
        }

        //是否显示下一页
        if(page == totalPage){
            showNext = false;
        }else {
            showNext = true;
        }

        if(pages.contains(1)){
            showFirstPage = false;
        }else {
            showFirstPage = true;
        }

        if(pages.contains(totalPage)){
            showEndPage = false;
        }else {
            showEndPage = true;
        }
    }
}
