package fate.utils.echarts;

import java.util.ArrayList;
import java.util.List;

public class Pie {

	private String title;
	
	private List<String> legend = new ArrayList<>();
	
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

	public List<Series> getSeries() {
		return series;
	}

	public void setSeries(List<Series> series) {
		this.series = series;
	}

	public void newSeries(String name, int value){
		Series s = new Series();
		s.setName(name);
		s.setValue(value);
		series.add(s);
	}

	class Series {

		private String name;
		
		private int value;
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}
	
	
}
