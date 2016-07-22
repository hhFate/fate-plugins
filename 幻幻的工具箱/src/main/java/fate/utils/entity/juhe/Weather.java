package fate.utils.entity.juhe;

public class Weather {

	private int resultcode;
	
	private String reason;
	
	private int error_code;
	
	private Result result;
	
	public int getResultcode() {
		return resultcode;
	}

	public void setResultcode(int resultcode) {
		this.resultcode = resultcode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getError_code() {
		return error_code;
	}

	public void setError_code(int error_code) {
		this.error_code = error_code;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public class Result{
		private Sk sk;
		
		private Today today;

		public Sk getSk() {
			return sk;
		}

		public void setSk(Sk sk) {
			this.sk = sk;
		}

		public Today getToday() {
			return today;
		}

		public void setToday(Today today) {
			this.today = today;
		}
		
	}
	
	public class Sk{
		/**
		 * 温度
		 */
		private String temp;
		
		/**
		 * 风向
		 */
		private String windDirection;
		
		/**
		 * 风力
		 */
		private String windStrength;
		
		/**
		 * 湿度
		 */
		private String humidity;
		
		/**
		 * 更新时间
		 */
		private String time;

		public String getTemp() {
			return temp;
		}

		public void setTemp(String temp) {
			this.temp = temp;
		}

		public String getWindDirection() {
			return windDirection;
		}

		public void setWindDirection(String windDirection) {
			this.windDirection = windDirection;
		}

		public String getWindStrength() {
			return windStrength;
		}

		public void setWindStrength(String windStrength) {
			this.windStrength = windStrength;
		}

		public String getHumidity() {
			return humidity;
		}

		public void setHumidity(String humidity) {
			this.humidity = humidity;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}
	}
	
	public class Today{
		private String city;
		
		private String date_y;
		
		private String week;
		
		private String temperature;
		
		private String weather;
		
		private String wind;
		
		private String dressing_index;
		
		private String dressing_advice;
		
		private String uv_index;
		
		private String comfort_index;
		
		private String wash_index;
		
		private String travel_index;
		
		private String exercise_index;
		
		private String drying_index;

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getDate_y() {
			return date_y;
		}

		public void setDate_y(String date_y) {
			this.date_y = date_y;
		}

		public String getWeek() {
			return week;
		}

		public void setWeek(String week) {
			this.week = week;
		}

		public String getTemperature() {
			return temperature;
		}

		public void setTemperature(String temperature) {
			this.temperature = temperature;
		}

		public String getWeather() {
			return weather;
		}

		public void setWeather(String weather) {
			this.weather = weather;
		}

		public String getWind() {
			return wind;
		}

		public void setWind(String wind) {
			this.wind = wind;
		}

		public String getDressing_index() {
			return dressing_index;
		}

		public void setDressing_index(String dressing_index) {
			this.dressing_index = dressing_index;
		}

		public String getDressing_advice() {
			return dressing_advice;
		}

		public void setDressing_advice(String dressing_advice) {
			this.dressing_advice = dressing_advice;
		}

		public String getUv_index() {
			return uv_index;
		}

		public void setUv_index(String uv_index) {
			this.uv_index = uv_index;
		}

		public String getComfort_index() {
			return comfort_index;
		}

		public void setComfort_index(String comfort_index) {
			this.comfort_index = comfort_index;
		}

		public String getWash_index() {
			return wash_index;
		}

		public void setWash_index(String wash_index) {
			this.wash_index = wash_index;
		}

		public String getTravel_index() {
			return travel_index;
		}

		public void setTravel_index(String travel_index) {
			this.travel_index = travel_index;
		}

		public String getExercise_index() {
			return exercise_index;
		}

		public void setExercise_index(String exercise_index) {
			this.exercise_index = exercise_index;
		}

		public String getDrying_index() {
			return drying_index;
		}

		public void setDrying_index(String drying_index) {
			this.drying_index = drying_index;
		}
		
		
	}
}
