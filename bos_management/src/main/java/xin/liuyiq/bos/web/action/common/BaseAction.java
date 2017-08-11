package xin.liuyiq.bos.web.action.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class BaseAction<T> extends ActionSupport implements ModelDriven<T>{
	
	private static final String TOTAL = "total";
	private static final String ROWS = "rows";
	private final Map<String,Object> result = new HashMap<String,Object>();
	
    protected T model;
    
    @Override
    public T getModel() {
        return model;
    }
    
    public BaseAction(){
        // 在调用这个构造的时候来获取泛型
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        try {
            model = modelClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // area_pageQuery
    protected Integer page;
    protected Integer rows;

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
    

	public void responseJsonDataToPage(Page pageBean){
        this.result.put(this.TOTAL, pageBean.getTotalElements());
        this.result.put(this.ROWS, pageBean.getContent());
        pushObjectToValueStack(this.result);
    }
	
	public void pushObjectToValueStack(Object param){
		 ActionContext.getContext().getValueStack().push(param);
	}
}
