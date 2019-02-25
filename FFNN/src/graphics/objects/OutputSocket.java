package graphics.objects;

public class OutputSocket<T> extends Socket<T> {
	
	private InputSocket<T> connection;

	public void sendValue() {
		if (connection != null) {
			connection.setValue(getValue());
		}
	}
	
	public boolean hasConnection() {
		if (connection == null) {
			return false;
		} else {
			return true;
		}
	}

	public InputSocket<T> getConnection() {
		return connection;
	}

	public void setConnection(InputSocket<T> connection) {
		this.connection = connection;
	}

}
