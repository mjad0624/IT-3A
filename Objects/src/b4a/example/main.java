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

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = true;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;
    public static boolean dontPause;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.example", "b4a.example.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
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
		activityBA = new BA(this, layout, processBA, "b4a.example", "b4a.example.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.example.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
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
		return main.class;
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
            BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (main) Pause event (activity is not paused). **");
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
            main mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
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
public static anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public static anywheresoftware.b4a.sql.SQL _sql = null;
public static String _mypass = "";
public static String _myfname = "";
public static String _mylevel = "";
public static String _myuname = "";
public static int _myid = 0;
public static String _mylname = "";
public anywheresoftware.b4a.objects.EditTextWrapper _etuname = null;
public anywheresoftware.b4a.objects.EditTextWrapper _etpassword = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnlogin = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnclose = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _checkbox1 = null;
public b4a.example.dateutils _dateutils = null;
public b4a.example.starter _starter = null;
public b4a.example.adminform _adminform = null;
public b4a.example.updateuser _updateuser = null;
public b4a.example.addusers _addusers = null;
public b4a.example.teacherform _teacherform = null;
public b4a.example.studentform _studentform = null;
public b4a.example.about _about = null;
public b4a.example.profile _profile = null;
public b4a.example.xuiviewsutils _xuiviewsutils = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (adminform.mostCurrent != null);
vis = vis | (updateuser.mostCurrent != null);
vis = vis | (addusers.mostCurrent != null);
vis = vis | (teacherform.mostCurrent != null);
vis = vis | (studentform.mostCurrent != null);
vis = vis | (about.mostCurrent != null);
vis = vis | (profile.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 37;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 38;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 39;BA.debugLine="copydbifneeded(\"database.db\")";
_copydbifneeded("database.db");
 //BA.debugLineNum = 40;BA.debugLine="sql.Initialize(File.DirInternal,\"database.db\",Fa";
_sql.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"database.db",anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 42;BA.debugLine="Activity.LoadLayout(\"layout\")";
mostCurrent._activity.LoadLayout("layout",mostCurrent.activityBA);
 //BA.debugLineNum = 44;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 56;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 58;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 52;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 54;BA.debugLine="End Sub";
return "";
}
public static String  _btnclose_click() throws Exception{
 //BA.debugLineNum = 98;BA.debugLine="Sub btnclose_Click";
 //BA.debugLineNum = 99;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 100;BA.debugLine="End Sub";
return "";
}
public static String  _btnlogin_click() throws Exception{
String _qry = "";
anywheresoftware.b4a.sql.SQL.CursorWrapper _c1 = null;
 //BA.debugLineNum = 61;BA.debugLine="Sub btnlogin_Click";
 //BA.debugLineNum = 62;BA.debugLine="Dim qry As String";
_qry = "";
 //BA.debugLineNum = 63;BA.debugLine="Dim c1 As Cursor";
_c1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 66;BA.debugLine="qry = \"Select * from users where uname = ?\"";
_qry = "Select * from users where uname = ?";
 //BA.debugLineNum = 67;BA.debugLine="c1 = sql.ExecQuery2(qry,Array As String(etuname.T";
_c1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_sql.ExecQuery2(_qry,new String[]{mostCurrent._etuname.getText()})));
 //BA.debugLineNum = 68;BA.debugLine="If c1.RowCount>0 Then";
if (_c1.getRowCount()>0) { 
 //BA.debugLineNum = 69;BA.debugLine="c1.Position = 0";
_c1.setPosition((int) (0));
 //BA.debugLineNum = 70;BA.debugLine="mypass = c1.GetString(\"password\")";
_mypass = _c1.GetString("password");
 //BA.debugLineNum = 71;BA.debugLine="myfname = c1.GetString(\"fname\")";
_myfname = _c1.GetString("fname");
 //BA.debugLineNum = 72;BA.debugLine="mylname = c1.GetString(\"lname\")";
_mylname = _c1.GetString("lname");
 //BA.debugLineNum = 73;BA.debugLine="myid = c1.GetString(\"id\")";
_myid = (int)(Double.parseDouble(_c1.GetString("id")));
 //BA.debugLineNum = 74;BA.debugLine="myuname = c1.GetString(\"uname\")";
_myuname = _c1.GetString("uname");
 //BA.debugLineNum = 75;BA.debugLine="mylevel = c1.GetString(\"level\")";
_mylevel = _c1.GetString("level");
 //BA.debugLineNum = 78;BA.debugLine="If mypass = etpassword.Text Then";
if ((_mypass).equals(mostCurrent._etpassword.getText())) { 
 //BA.debugLineNum = 79;BA.debugLine="Msgbox(\"Welcome \"&myfname&\" \"&mylname,\"\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Welcome "+_myfname+" "+_mylname),BA.ObjectToCharSequence(""),mostCurrent.activityBA);
 //BA.debugLineNum = 80;BA.debugLine="etpassword.Text = \"\"";
mostCurrent._etpassword.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 81;BA.debugLine="etuname.Text = \"\"";
mostCurrent._etuname.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 82;BA.debugLine="If mylevel = 1 Then";
if ((_mylevel).equals(BA.NumberToString(1))) { 
 //BA.debugLineNum = 83;BA.debugLine="StartActivity(\"adminform\")";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)("adminform"));
 }else if((_mylevel).equals(BA.NumberToString(2))) { 
 //BA.debugLineNum = 85;BA.debugLine="StartActivity(\"teacherform\")";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)("teacherform"));
 }else {
 //BA.debugLineNum = 87;BA.debugLine="StartActivity(\"studentform\")";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)("studentform"));
 };
 }else {
 //BA.debugLineNum = 90;BA.debugLine="Msgbox(\"Wrong Password\",\"\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Wrong Password"),BA.ObjectToCharSequence(""),mostCurrent.activityBA);
 };
 }else {
 //BA.debugLineNum = 93;BA.debugLine="Msgbox(\"No User Found\",\"\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("No User Found"),BA.ObjectToCharSequence(""),mostCurrent.activityBA);
 };
 //BA.debugLineNum = 96;BA.debugLine="End Sub";
return "";
}
public static String  _checkbox1_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 102;BA.debugLine="Sub CheckBox1_CheckedChange(Checked As Boolean)";
 //BA.debugLineNum = 103;BA.debugLine="If CheckBox1.Checked= True Then";
if (mostCurrent._checkbox1.getChecked()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 104;BA.debugLine="etpassword.PasswordMode = False";
mostCurrent._etpassword.setPasswordMode(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 106;BA.debugLine="etpassword.PasswordMode = True";
mostCurrent._etpassword.setPasswordMode(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 108;BA.debugLine="End Sub";
return "";
}
public static String  _copydbifneeded(String _filename) throws Exception{
 //BA.debugLineNum = 46;BA.debugLine="Sub copydbifneeded(Filename As String)";
 //BA.debugLineNum = 48;BA.debugLine="If File.Exists(File.DirInternal,Filename) = False";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_filename)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 49;BA.debugLine="File.Copy(File.DirAssets,Filename,File.DirIntern";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),_filename,anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_filename);
 };
 //BA.debugLineNum = 51;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 28;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 30;BA.debugLine="Private etuname As EditText";
mostCurrent._etuname = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private etpassword As EditText";
mostCurrent._etpassword = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private btnlogin As Button";
mostCurrent._btnlogin = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private btnclose As Button";
mostCurrent._btnclose = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private CheckBox1 As CheckBox";
mostCurrent._checkbox1 = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 35;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        b4a.example.dateutils._process_globals();
main._process_globals();
starter._process_globals();
adminform._process_globals();
updateuser._process_globals();
addusers._process_globals();
teacherform._process_globals();
studentform._process_globals();
about._process_globals();
profile._process_globals();
xuiviewsutils._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 18;BA.debugLine="Private xui As XUI";
_xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 19;BA.debugLine="Dim sql As SQL";
_sql = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 20;BA.debugLine="Dim mypass As String";
_mypass = "";
 //BA.debugLineNum = 21;BA.debugLine="Dim myfname As String";
_myfname = "";
 //BA.debugLineNum = 22;BA.debugLine="Dim mylevel As String";
_mylevel = "";
 //BA.debugLineNum = 23;BA.debugLine="Dim myuname As String";
_myuname = "";
 //BA.debugLineNum = 24;BA.debugLine="Dim myid As Int";
_myid = 0;
 //BA.debugLineNum = 25;BA.debugLine="Dim mylname As String";
_mylname = "";
 //BA.debugLineNum = 26;BA.debugLine="End Sub";
return "";
}
}
