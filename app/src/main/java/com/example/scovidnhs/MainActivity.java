package com.example.scovidnhs;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class MainActivity extends AppCompatActivity implements LocationListener {

	//private TextView textView, cityTV,countryTV;
	LocationManager locationManager;
	private double latitude;
	private double longitude;
	String data;
	String city;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		//final ScrollView sv = (ScrollView) findViewById(R.id.scrollView1);
		final TableLayout tl = (TableLayout) findViewById(R.id.main_table);

		//textView = findViewById(R.id.textview);
		//countryTV = findViewById(R.id.country);
		//cityTV = findViewById(R.id.city);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}

		Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		onLocationChanged(location);

		loc_func(location);

		data = "";

		RequestQueue queue = Volley.newRequestQueue(this);
		//textView.setMovementMethod(new ScrollingMovementMethod());
		//String JsonURL = "https://raw.githubusercontent.com/ianbar20/JSON-Volley-Tutorial/master/Example-JSON-Files/Example-Object.JSON";
		// Request a string response from the provided URL.
		String jsonURI = "https://www.opendata.nhs.scot/api/3/action/datastore_search?resource_id=2dd8534b-0a6f-4744-9253-9565d62f96c2";
		//String jsonURI ="https://www.opendata.nhs.scot/api/3/action/datastore_search?resource_id=7fad90e5-6f19-455b-bc07-694a22f8d5dc";
		final JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, jsonURI, null,
						new Response.Listener <JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {

								try {


									//this is the data request
									JSONObject obj = response.getJSONObject("result");
									// Retrieves the string labeled "colorName" and "description" from
									//the response JSON Object
									//and converts them into javascript objects
									//String color = obj.getString("fields");
									//String desc = obj.getString("records");


									String l = city.toUpperCase();
									//String l = "";
									//String test = "Glasgow City";
									//textview.append(l);
									//String l = "Glasgow City";
									JSONArray records = obj.getJSONArray("records");


									int count = 0;
									//maybe use next() in ref saved
									for (int i = 0; i < records.length(); i++) {

										JSONObject record = records.getJSONObject(i);

										String hb = record.getString("HBName").toUpperCase();
										String id = record.getString("_id");
										String Date = record.getString("Date");
										String DailyDeaths = record.getString("DailyDeaths");
										String TotalTests = record.getString("TotalTests");
										String dailyPositives = record.getString("DailyPositive");
										String HospitalAdmissions = record.getString("HospitalAdmissions");
										String PositivePercentage = record.getString("PositivePercentage");
										String PositivePercentage7Day = record.getString("PositivePercentage7Day");

										TextView[] textArray = new TextView[records.length()];

										TableRow[] tr_head = new TableRow[records.length()];
										//Create the tablerows
										ScrollView scroll = new ScrollView(getApplicationContext());

										if (l.length() > 1) {

											String[] sc = l.split(" ");


											for (int x = 0; x < sc.length; x++) {

												//int count = 0;

												if (hb.contains(sc[x])) {


													tr_head[x] = new TableRow(getApplicationContext());
													tr_head[x].setId(i + 1);


													tr_head[x].setLayoutParams(new TableLayout.LayoutParams(
																	TableLayout.LayoutParams.MATCH_PARENT,
																	TableLayout.LayoutParams.WRAP_CONTENT));

													textArray[x] = new TextView(getApplicationContext());
													textArray[x].setId(i + 111);
													textArray[x].setText(hb + "\n" + "Date:" + Date + "\n" + "Daily Deaths:" + DailyDeaths + "\n" + "Total Test:" + TotalTests + "\n" + "Daily Positive:" + dailyPositives + "\n" + "Hospital Admissions:" + HospitalAdmissions + "\n" + "Positive Percentage:" + PositivePercentage + "\n" + "Positive Percentage 7 days:" + PositivePercentage7Day);
													//textArray[x].setBackgroundColor(Color.GREEN);
													//textArray[x].setTextColor(Color.WHITE);
													textArray[x].setTypeface(null, Typeface.BOLD);
													textArray[x].setPadding(20, 5, 0, 5);

													if (count % 2 == 1) {

														tr_head[x].setBackgroundColor(Color.parseColor("#e4d7a3"));

													} else {

														tr_head[x].setBackgroundColor(Color.parseColor("#a3e4d7"));

													}

													//textArray[x].setPadding(5, 5, 5, 5);
													tr_head[x].addView(textArray[x]);


													tl.addView(tr_head[x], new TableLayout.LayoutParams(
																	TableLayout.LayoutParams.MATCH_PARENT,
																	TableLayout.LayoutParams.WRAP_CONTENT));


												}//if Health Board from GEOLACTION MATCHES DATA THEN SHOW ONLY

											}// EDND OF FOR

											count = count + 1;
										} else {

/*
                                    tr_head[i] = new TableRow(getApplicationContext());
                                    tr_head[i].setId(i + 1);

                                    if (count % 2 == 0) {

                                        tr_head[i].setBackgroundColor(Color.parseColor("#A3E4D7"));

                                    } else {

                                        tr_head[i].setBackgroundColor(Color.parseColor("#e4d7a3"));

                                    }

                                    tr_head[i].setLayoutParams(new TableLayout.LayoutParams(
                                            TableLayout.LayoutParams.MATCH_PARENT,
                                            TableLayout.LayoutParams.WRAP_CONTENT));

                                    textArray[i] = new TextView(getApplicationContext());
                                    textArray[i].setId(i + 111);
                                    textArray[i].setTypeface(null, Typeface.BOLD);
                                    textArray[i].setText(hb + "\n" + "Date:" + " " + Date + "\n" + "Daily Deaths:" + " " + DailyDeaths + "\n" + "Total Test:" + " " + TotalTests + "\n" + "Daily Positive:" + " " + dailyPositives + "\n" + "Hospital Admissions:" + " " + HospitalAdmissions + "\n" + "Positive Percentage:" + " " + PositivePercentage + "\n" + "Positive Percentage 7 days:" + " " + PositivePercentage7Day);
                                    //textArray[x].setBackgroundColor(Color.GREEN);
                                    //textArray[x].setTextColor(Color.WHITE);
                                    textArray[i].setPadding(20, 5, 0, 5);


                                    //textArray[x].setPadding(5, 5, 5, 5);
                                    tr_head[i].addView(textArray[i]);


                                    tl.addView(tr_head[i], new TableLayout.LayoutParams(
                                            TableLayout.LayoutParams.MATCH_PARENT,
                                            TableLayout.LayoutParams.WRAP_CONTENT));
*/

										}

										//count = count + 1;

									}


								} catch (JSONException e) {
									// If an error occurs, this prints the error to the log
									e.printStackTrace();
								}

							}


						},


						error -> Log.e("Volley", "Error"));//end of json object


		// Add the request to the RequestQueue.
		queue.add(stringRequest);

	}//endofcreate









	@Override
	public void onLocationChanged(Location location) {

		latitude = location.getLatitude();
		longitude = location.getLongitude();

		//textView.append("Latitude"+ latitude+"/n"+"Longitude"+ longitude);


	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	private String loc_func(Location location) {

		Geocoder geocoder = new Geocoder(this);

		List <Address> address = null;

		try {
			address = geocoder.getFromLocation(latitude, longitude, 1);
			String country = address.get(0).getCountryName();
			city = address.get(0).getSubAdminArea();
			//getAdminArea() = COUNTRY
			//getSubAdminArea = city
			//getSubLocality() = NULL
			//getSubThoroughfare() = 668
			//getThoroughfare() = EGLINTON STREET
			//getPostalCode(); = G5 9RP
			//getPremises(); = NULL
			// getPhone() = NULL

			//countryTV.setText("Country:"+ country);
			//cityTV.setText(city.toString());


		} catch (IOException e) {
			e.printStackTrace();
		}

		return city;
	}


}
