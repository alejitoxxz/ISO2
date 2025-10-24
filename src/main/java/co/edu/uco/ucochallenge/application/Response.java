package co.edu.uco.ucochallenge.application;

public abstract class Response<T>{

	private boolean dataReturned;
	private T data;
	
	protected Response(final boolean returnData,final T data) {
		setDataReturned(returnData);
		setData(data);
	}

	private void setDataReturned(boolean dataReturned) {
		// Limpieza de datos o validaciones si es necesario
		this.dataReturned = dataReturned;
	}

	private void setData(T data) {
		// Limpieza de datos o validaciones si es necesario
		this.data = data;
	}

	public boolean isDataReturned() {
		return dataReturned;
	}

	public T getData() {
		return data;
	}
	
	
}
