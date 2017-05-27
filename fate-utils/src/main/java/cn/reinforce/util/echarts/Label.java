package cn.reinforce.util.echarts;

/**
 * @author Fate
 * @create 2017/3/15
 */
public class Label {

    private LabelItem normal;

    private LabelItem emphasis;

    public void setNormal(LabelItem normal) {
        this.normal = normal;
    }

    public void setEmphasis(LabelItem emphasis) {
        this.emphasis = emphasis;
    }

    public LabelItem getNormal() {
        return normal;
    }

    public LabelItem getEmphasis() {
        return emphasis;
    }
}
