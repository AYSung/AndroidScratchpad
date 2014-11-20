package cpsc112.studybuddy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class DisplayUsersActivity extends Activity {
	
	private Firebase rootRef;
	private AuthData authData;
	private TextView textView;
	private ProgressDialog loadingDialog;
	public String uID, courseFilter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Firebase.setAndroidContext(this);
		rootRef = new Firebase("https://scorching-heat-1838.firebaseio.com/");
		setContentView(R.layout.activity_display_users);

		Intent intent = getIntent();
		this.uID = intent.getStringExtra(CreateUserActivity.UID);

		textView = (TextView) findViewById(R.id.userList);
		textView.setMovementMethod(new ScrollingMovementMethod());
		textView.setTextSize(20);
		
		loadingDialog = new ProgressDialog(this);
		loadingDialog.setTitle("Loading");
		loadingDialog.setMessage("Searching for other users");
		loadingDialog.show();
	
		
		
		rootRef.child("users").child(uID).addValueEventListener(new ValueEventListener(){
			public void onDataChange(DataSnapshot snapshot){
				courseFilter = snapshot.child("course").getValue().toString();
				textView.setText("Users in " + courseFilter + ":\n\n");
				
				rootRef.child("classes").child(courseFilter).addChildEventListener(new ChildEventListener() {
					public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
						if (!snapshot.getKey().equals(uID)){
							textView.append(snapshot.getValue().toString() + "\n");
						}
					}
					
					public void onChildChanged(DataSnapshot snapshot, String previousChildKey) {
//						onChildAdded(snapshot, previousChildKey);
					}
					
					public void onChildRemoved(DataSnapshot snapshot) {
//						onChildAdded(snapshot, null);
					}
					
					public void onChildMoved(DataSnapshot snapshot, String previousChildKey) {
//						onChildAdded(snapshot, previousChildKey);
					}
					
					public void onCancelled(FirebaseError firebaseError){
						textView.setText("Could not retrieve other users =(");
					}
					
				});
				
				loadingDialog.hide();
				
//				rootRef.child("classes").child(courseFilter).addValueEventListener(new ValueEventListener() {
//					public void onDataChange(DataSnapshot snapshot){
//						
//						textView.setText("Others in " + courseFilter + ":\n\n");
//						
//						for (DataSnapshot user : snapshot.getChildren()){
//							if (!user.getKey().equals(uID)){
//								textView.append(user.getValue().toString() + "\n");
//							}
//						}
//						
//						loadingDialog.hide();
//			
//					}
//					
//					public void onCancelled(FirebaseError firebaseError){
//						
//					}
//					
//				});
				
				
			}
			
			public void onCancelled(FirebaseError firebaseError){
				
			}
		});
	
		
		
		
//		rootRef.child("users").addValueEventListener(new ValueEventListener() {
//			public void onDataChange(DataSnapshot snapshot){
//				
//				String courseFilter = snapshot.child(uID).child("course").getValue().toString();
//				
//				textView.setText("Others enrolled in " + courseFilter + ":\n\n");
//				
//				for (DataSnapshot user : snapshot.getChildren()){
//					@SuppressWarnings("unchecked")
//					Map<String, Object> userFields = (Map<String, Object>) user.getValue();
//					if (!user.getKey().equals(uID) && userFields.get("course").equals(courseFilter)){
//						textView.append(userFields.get("name").toString() + "\n");
//					}
//				}
//	
//			}
//			
//			public void onCancelled(FirebaseError firebaseError){
//				
//			}
//			
//		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_users, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void logoutUser (View view){
		logout();
		startActivity(new Intent(this, LoginActivity.class));
	}
	
    private void logout() {
        if (this.authData != null) {
            /* logout of Firebase */
            rootRef.unauth();
            /* Update authenticated user and show login buttons */
//            setAuthenticatedUser(null);
        }
    }
}
