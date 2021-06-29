package it.polito.tdp.yelp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.yelp.model.Adiacenza;
import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Review;
import it.polito.tdp.yelp.model.User;

public class YelpDao {

	public void getAllBusiness(Map<String,Business> idMap){
		String sql = "SELECT * FROM Business";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
                if(!idMap.containsKey(res.getString("business_id"))) {
				Business business = new Business(res.getString("business_id"), 
						res.getString("full_address"),
						res.getString("active"),
						res.getString("categories"),
						res.getString("city"),
						res.getInt("review_count"),
						res.getString("business_name"),
						res.getString("neighborhoods"),
						res.getDouble("latitude"),
						res.getDouble("longitude"),
						res.getString("state"),
						res.getDouble("stars"));
				idMap.put(res.getString("business_id"),business);
			}
			}
			res.close();
			st.close();
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		
		}
	}
	
	public List<Business> getVerici(String citta, int anno, Map<String,Business> idMap){
		String sql = "SELECT DISTINCT b.business_id "
				+ "FROM reviews r, business b "
				+ "WHERE b.business_id= r.business_id AND b.city= ? AND YEAR(r.review_date)= ? ";
		List<Business> result = new ArrayList<Business>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, citta);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(idMap.containsKey(res.getString("business_id"))) {
					Business business = idMap.get(res.getString("business_id"));
					result.add(business);
				}
				}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Review> getAllReviews(){
		String sql = "SELECT * FROM Reviews";
		List<Review> result = new ArrayList<Review>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Review review = new Review(res.getString("review_id"), 
						res.getString("business_id"),
						res.getString("user_id"),
						res.getDouble("stars"),
						res.getDate("review_date").toLocalDate(),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("review_text"));
				result.add(review);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<User> getAllUsers(){
		String sql = "SELECT * FROM Users";
		List<User> result = new ArrayList<User>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				User user = new User(res.getString("user_id"),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("name"),
						res.getDouble("average_stars"),
						res.getInt("review_count"));
				
				result.add(user);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getAllCities(){
		String sql = "SELECT  DISTINCT city "
				+ "FROM business ";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				String city= res.getString("city");
				result.add(city);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenza> getAdiacenze(String citta, int anno, Map<String,Business> idMap){
		String sql = "SELECT  b1.business_id AS b1, b2.business_id AS b2, (AVG(r.stars)-AVG(r2.stars)) AS Peso "
				+ "FROM reviews r, reviews r2, business b1, business b2 "
				+ "WHERE b1.business_id= r.business_id AND b2.business_id=r2.business_id AND b1.city= ? AND b1.city= b2.city AND "
				+ "b1.business_id<> b2.business_id AND YEAR(r.review_date)= ? AND YEAR(r.review_date)=YEAR(r2.review_date) "
				+ "GROUP BY b1.business_id , b2.business_id "
				+ "HAVING Peso<>0 ";
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, citta);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(idMap.containsKey(res.getString("b1")) && idMap.containsKey(res.getString("b2"))) {
					Business b1= idMap.get(res.getString("b1"));
					Business b2= idMap.get(res.getString("b2"));
					Double peso= res.getDouble("Peso");
					Adiacenza a = new Adiacenza(b1,b2,peso);
					result.add(a);
				}
				
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
