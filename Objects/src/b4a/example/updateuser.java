package b4a.example;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class updateuser extends Activity implements B4AActivity{
	public static updateuser mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;
    public static boolean dontPause;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.example", "b4a.example.updateuser");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (updateuser).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(this, processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "b4a.example", "b4a.example.updateuser");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.example.updateuser", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (updateuser) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (updateuser) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return updateuser.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        if (!dontPause)
            BA.LogInfo("** Activity (updateuser) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (updateuser) Pause event (activity is not paused). **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        if (!dontPause) {
            processBA.setActivityPaused(true);
            mostCurrent = null;
        }

        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
            updateuser mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (updateuser) Resume **");
            if (mc != mostCurrent)
                return;
		    processBA.raiseEvent(mc._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static String _uid = "";
public static String _uname = "";
public static String _upass = "";
public static String _ufname = "";
public static String _ulname = "";
public static String _ulevel = "";
public static anywheresoftware.b4a.sql.SQL _sql = null;
public anywheresoftware.b4a.objects.EditTextWrapper _etid = null;
public anywheresoftware.b4a.objects.EditTextWrapper _etuname = null;
public anywheresoftware.b4a.objects.EditTextWrapper _etpass = null;
public anywheresoftware.b4a.objects.EditTextWrapper _etfname = null;
public anywheresoftware.b4a.objects.EditTextWrapper _etlname = null;
public anywheresoftware.b4a.objects.EditTextWrapper _etlevel = null;
public b4a.example.b4xdrawer _drawer = null;
public b4a.example.dateutils _dateutils = null;
public b4a.example.main _main = null;
public b4a.example.starter _starter = null;
public b4a.example.adminform _adminform = null;
public b4a.example.addusers _addusers = null;
public b4a.example.teacherform _teacherform = null;
public b4a.example.studentform _studentform = null;
public b4a.example.about _about = null;
public b4a.example.profile _profile = null;
public b4a.example.xuiviewsutils _xuiviewsutils = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 32;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 33;BA.debugLine="Activity.LoadLayout(\"updateuser\")";
mostCurrent._activity.LoadLayout("updateuser",mostCurrent.activityBA);
 //BA.debugLineNum = 34;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 35;BA.debugLine="copydbifneeded(\"database.db\")";
_copydbifneeded("database.db");
 //BA.debugLineNum = 36;BA.debugLine="sql.Initialize(File.DirInternal,\"database.db\",Fa";
_sql.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"database.db",anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 40;BA.debugLine="etid.Text = uid";
mostCurrent._etid.setText(BA.ObjectToCharSequence(_uid));
 //BA.debugLineNum = 41;BA.debugLine="etuname.Text = uname";
mostCurrent._etuname.setText(BA.ObjectToCharSequence(_uname));
 //BA.debugLineNum = 42;BA.debugLine="etpass.Text = upass";
mostCurrent._etpass.setText(BA.ObjectToCharSequence(_upass));
 //BA.debugLineNum = 43;BA.debugLine="etfname.Text = ufname";
mostCurrent._etfname.setText(BA.ObjectToCharSequence(_ufname));
 //BA.debugLineNum = 44;BA.debugLine="etlname.Text = ulname";
mostCurrent._etlname.setText(BA.ObjectToCharSequence(_ulname));
 //BA.debugLineNum = 45;BA.debugLine="etlevel.Text = ulevel";
mostCurrent._etlevel.setText(BA.ObjectToCharSequence(_ulevel));
 //BA.debugLineNum = 47;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 53;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 55;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 49;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 51;BA.debugLine="End Sub";
return "";
}
public static String  _btncancel_click() throws Exception{
 //BA.debugLineNum = 86;BA.debugLine="Sub btncancel_Click";
 //BA.debugLineNum = 87;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 88;BA.debugLine="End Sub";
return "";
}
public static String  _btnupdate_click() throws Exception{
String _qry = "";
String _ans = "";
 //BA.debugLineNum = 63;BA.debugLine="Sub btnupdate_Click";
 //BA.debugLineNum = 64;BA.debugLine="Dim qry As String";
_qry = "";
 //BA.debugLineNum = 65;BA.debugLine="Dim ans As String";
_ans = "";
 //BA.debugLineNum = 67;BA.debugLine="ans = Msgbox2(\"Save Changes?\",\"Update Records\",\"O";
_ans = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Save Changes?"),BA.ObjectToCharSequence("Update Records"),"Ok","Cancel","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 //BA.debugLineNum = 68;BA.debugLine="If ans = DialogResponse.POSITIVE Then";
if ((_ans).equals(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE))) { 
 //BA.debugLineNum = 69;BA.debugLine="If etuname.Text = \"\" Or etpass.Text = \"\"  Or etf";
if ((mostCurrent._etuname.getText()).equals("") || (mostCurrent._etpass.getText()).equals("") || (mostCurrent._etfname.getText()).equals("") || (mostCurrent._etlname.getText()).equals("") || (mostCurrent._etlevel.getText()).equals("")) { 
 //BA.debugLineNum = 70;BA.debugLine="Msgbox(\"Please Fill all required data\",\"Error\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Please Fill all required data"),BA.ObjectToCharSequence("Error"),mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 72;BA.debugLine="If etlevel.Text > 4 Or etlevel.Text <= 0 Then";
if ((double)(Double.parseDouble(mostCurrent._etlevel.getText()))>4 || (double)(Double.parseDouble(mostCurrent._etlevel.getText()))<=0) { 
 //BA.debugLineNum = 73;BA.debugLine="Msgbox(\"Invalid Level\",\"Error\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Invalid Level"),BA.ObjectToCharSequence("Error"),mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 75;BA.debugLine="qry = \"Update users set uname = ?, password =";
_qry = "Update users set uname = ?, password = ?, fname = ?, lname = ?, level = ? where id = "+mostCurrent._etid.getText();
 //BA.debugLineNum = 76;BA.debugLine="sql.ExecNonQuery2(qry, Array As String (etunam";
_sql.ExecNonQuery2(_qry,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{mostCurrent._etuname.getText(),mostCurrent._etpass.getText(),mostCurrent._etfname.getText(),mostCurrent._etlname.getText(),mostCurrent._etlevel.getText()}));
 //BA.debugLineNum = 77;BA.debugLine="Msgbox(\"User Successfully Updated\",\"\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("User Successfully Updated"),BA.ObjectToCharSequence(""),mostCurrent.activityBA);
 //BA.debugLineNum = 78;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 };
 };
 }else {
 //BA.debugLineNum = 82;BA.debugLine="Msgbox(\"Update cancelled)\",\"\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Update cancelled)"),BA.ObjectToCharSequence(""),mostCurrent.activityBA);
 };
 //BA.debugLineNum = 84;BA.debugLine="End Sub";
return "";
}
public static String  _copydbifneeded(String _filename) throws Exception{
 //BA.debugLineNum = 57;BA.debugLine="Sub copydbifneeded(Filename As String)";
 //BA.debugLineNum = 58;BA.debugLine="If File.Exists(File.DirInternal,Filename) = False";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_filename)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 59;BA.debugLine="File.Copy(File.DirAssets,Filename,File.DirIntern";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),_filename,anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_filename);
 };
 //BA.debugLineNum = 61;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 18;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 22;BA.debugLine="Private etid As EditText";
mostCurrent._etid = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private etuname As EditText";
mostCurrent._etuname = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private etpass As EditText";
mostCurrent._etpass = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private etfname As EditText";
mostCurrent._etfname = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private etlname As EditText";
mostCurrent._etlname = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private etlevel As EditText";
mostCurrent._etlevel = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private drawer as B4XDrawer";
mostCurrent._drawer = new b4a.example.b4xdrawer();
 //BA.debugLineNum = 30;BA.debugLine="End Sub";
return "";
}
public static String  _label8_click() throws Exception{
 //BA.debugLineNum = 90;BA.debugLine="Sub Label8_Click";
 //BA.debugLineNum = 91;BA.debugLine="drawer.LeftOpen = True";
mostCurrent._drawer._setleftopen /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 92;BA.debugLine="End Sub";
return "";
}
public static String  _lblabout_click() throws Exception{
 //BA.debugLineNum = 125;BA.debugLine="Sub lblabout_Click";
 //BA.debugLineNum = 126;BA.debugLine="drawer.LeftOpen = False";
mostCurrent._drawer._setleftopen /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 127;BA.debugLine="StartActivity(\"about\")";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)("about"));
 //BA.debugLineNum = 128;BA.debugLine="End Sub";
return "";
}
public static String  _lblhome_click() throws Exception{
 //BA.debugLineNum = 109;BA.debugLine="Sub lblhome_Click";
 //BA.debugLineNum = 110;BA.debugLine="If Main.mylevel = 1 Then";
if ((mostCurrent._main._mylevel /*String*/ ).equals(BA.NumberToString(1))) { 
 //BA.debugLineNum = 111;BA.debugLine="drawer.LeftOpen = False";
mostCurrent._drawer._setleftopen /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 112;BA.debugLine="StartActivity(\"adminform\")";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)("adminform"));
 }else if((mostCurrent._main._mylevel /*String*/ ).equals(BA.NumberToString(2))) { 
 //BA.debugLineNum = 115;BA.debugLine="drawer.LeftOpen = False";
mostCurrent._drawer._setleftopen /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 116;BA.debugLine="StartActivity(\"teacherform\")";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)("teacherform"));
 }else {
 //BA.debugLineNum = 119;BA.debugLine="drawer.LeftOpen = False";
mostCurrent._drawer._setleftopen /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 120;BA.debugLine="StartActivity(\"studentform\")";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)("studentform"));
 };
 //BA.debugLineNum = 123;BA.debugLine="End Sub";
return "";
}
public static String  _lbllogout_click() throws Exception{
int _msg = 0;
 //BA.debugLineNum = 100;BA.debugLine="Sub lbllogout_Click";
 //BA.debugLineNum = 101;BA.debugLine="Dim msg As Int";
_msg = 0;
 //BA.debugLineNum = 102;BA.debugLine="msg = Msgbox2(\"Do You Want To Logout?\",\"\",\"Yes\",\"";
_msg = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Do You Want To Logout?"),BA.ObjectToCharSequence(""),"Yes","No","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 103;BA.debugLine="If msg = DialogResponse.POSITIVE Then";
if (_msg==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 104;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 105;BA.debugLine="StartActivity(Main)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._main.getObject()));
 };
 //BA.debugLineNum = 107;BA.debugLine="End Sub";
return "";
}
public static String  _lblme_click() throws Exception{
 //BA.debugLineNum = 95;BA.debugLine="Sub lblme_Click";
 //BA.debugLineNum = 96;BA.debugLine="drawer.LeftOpen = False";
mostCurrent._drawer._setleftopen /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 97;BA.debugLine="StartActivity(\"profile\")";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)("profile"));
 //BA.debugLineNum = 98;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim	uid As String";
_uid = "";
 //BA.debugLineNum = 10;BA.debugLine="Dim uname As String";
_uname = "";
 //BA.debugLineNum = 11;BA.debugLine="Dim upass As String";
_upass = "";
 //BA.debugLineNum = 12;BA.debugLine="Dim ufname As String";
_ufname = "";
 //BA.debugLineNum = 13;BA.debugLine="Dim ulname As String";
_ulname = "";
 //BA.debugLineNum = 14;BA.debugLine="Dim ulevel As String";
_ulevel = "";
 //BA.debugLineNum = 15;BA.debugLine="Dim sql As SQL";
_sql = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 16;BA.debugLine="End Sub";
return "";
}
}
