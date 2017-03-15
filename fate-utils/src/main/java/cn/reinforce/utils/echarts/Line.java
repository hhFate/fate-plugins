package cn.reinforce.utils.echarts;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fate on 2016/6/15.
 */
public class Line {
    private String title;

    private List<String> legend = new ArrayList<>();

    private List<String> xAxis = new ArrayList<>();

    private List<Series> series = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getLegend() {
        return legend;
    }

    public void setLegend(List<String> legend) {
        this.legend = legend;
    }

    public List<String> getxAxis() {
        return xAxis;
    }

    public void setxAxis(List<String> xAxis) {
        this.xAxis = xAxis;
    }

    public List<Series> getSeries() {
        return series;
    }

    public void setSeries(List<Series> series) {
        this.series = series;
    }

    public class Series {

        private String name;

        private String type;

        private String stack;

        private List<Double> data = new ArrayList<>();

        private ItemStyle itemStyle;

        private AreaStyle areaStyle;

        private LineStyle lineStyle;

        private MarkLine markLine;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStack() {
            return stack;
        }

        public void setStack(String stack) {
            this.stack = stack;
        }

        public List<Double> getData() {
            return data;
        }

        public void setData(List<Double> data) {
            this.data = data;
        }

        public AreaStyle getAreaStyle() {
            return areaStyle;
        }

        public void setAreaStyle(AreaStyle areaStyle) {
            this.areaStyle = areaStyle;
        }

        public MarkLine getMarkLine() {
            return markLine;
        }

        public void setMarkLine(MarkLine markLine) {
            this.markLine = markLine;
        }

        public LineStyle getLineStyle() {
            return lineStyle;
        }

        public void setLineStyle(LineStyle lineStyle) {
            this.lineStyle = lineStyle;
        }

        public ItemStyle getItemStyle() {
            return itemStyle;
        }

        public void setItemStyle(ItemStyle itemStyle) {
            this.itemStyle = itemStyle;
        }
    }

    public class AreaStyle {
        private Style normal;

        private Style emphasis;

        public Style getNormal() {
            return normal;
        }

        public void setNormal(Style normal) {
            this.normal = normal;
        }

        public Style getEmphasis() {
            return emphasis;
        }

        public void setEmphasis(Style emphasis) {
            this.emphasis = emphasis;
        }
    }

    public class ItemStyle {
        private Style normal;

        private Style emphasis;

        public Style getNormal() {
            return normal;
        }

        public void setNormal(Style normal) {
            this.normal = normal;
        }

        public Style getEmphasis() {
            return emphasis;
        }

        public void setEmphasis(Style emphasis) {
            this.emphasis = emphasis;
        }
    }

    public class LineStyle {
        private Style normal;

        public Style getNormal() {
            return normal;
        }

        public void setNormal(Style normal) {
            this.normal = normal;
        }
    }

    public class Style{
        private String color;

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    public class MarkLine{
        private List<MarkLineData> data = new ArrayList<>();

        public List<MarkLineData> getData() {
            return data;
        }

        public void setData(List<MarkLineData> data) {
            this.data = data;
        }
    }

    public class MarkLineData{
        private String type;

        private int yAxis;

        private String name;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getyAxis() {
            return yAxis;
        }

        public void setyAxis(int yAxis) {
            this.yAxis = yAxis;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
