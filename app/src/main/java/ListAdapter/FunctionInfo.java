package ListAdapter;

/**
 * Created by 123 on 2016/4/1.
 */
public class FunctionInfo {
    private final int title;
    private final int description;
    private final Class activity;

    public FunctionInfo (int title,int description,Class activity){
        this.title = title;
        this.description = description;
        this.activity = activity;
    }

    public int getTitle(){
        return title;
    }

    public int getDescription(){
        return description;
    }

    public Class getActivity() {
        return activity;
    }
}
