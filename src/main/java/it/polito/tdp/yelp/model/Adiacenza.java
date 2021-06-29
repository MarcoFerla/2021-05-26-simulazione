package it.polito.tdp.yelp.model;

public class Adiacenza {

	private Business b1;
	private Business b2;
	private double peso;
	public Adiacenza(Business b1, Business b2, double peso) {
		super();
		this.b1 = b1;
		this.b2 = b2;
		this.peso = peso;
	}
	public Business getB1() {
		return b1;
	}
	public void setB1(Business b1) {
		this.b1 = b1;
	}
	public Business getB2() {
		return b2;
	}
	public void setB2(Business b2) {
		this.b2 = b2;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return b1 + " " + b2 + " " + peso + "\n";
	}
	
	
	
}
