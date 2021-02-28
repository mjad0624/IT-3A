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

public class teacherform extends Activity implements B4AActivity{
	public static teacherform mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.example", "b4a.example.teacherform");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (teacherform).");
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
		activityBA = new BA(this, layout, processBA, "b4a.example", "b4a.example.teacherform");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.example.teacherform", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (teacherform) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (teacherform) Resume **");
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
		return teacherform.class;
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
            BA.LogInfo("** Activity (teacherform) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (teacherform) Pause event (activity is not paused). **");
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
            teacherform mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (teacherform) Resume **");
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
public static anywheresoftware.b4a.sql.SQL _sql = null;
public anywheresoftware.b4a.objects.EditTextWrapper _etsname = null;
public b4a.example.b4xtable _b4xtable1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnadd = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnupdate = null;
public anywheresoftware.b4a.agraham.dialogs.InputDialog _inpdlg = null;
public b4a.example.b4xdrawer _drawer = null;
public anywheresoftware.b4a.objects.LabelWrapper _label2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblabout = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblhome = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllogout = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblme = null;
public b4a.example.dateutils _dateutils = null;
public b4a.example.main _main = null;
public b4a.example.starter _starter = null;
public b4a.example.adminform _adminform = null;
public b4a.example.updateuser _updateuser = null;
public b4a.example.addusers _addusers = null;
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
 //BA.debugLineNum = 29;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 31;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 32;BA.debugLine="copydbifneeded(\"database.db\")";
_copydbifneeded("database.db");
 //BA.debugLineNum = 33;BA.debugLine="sql.Initialize(File.DirInternal,\"database.db\",Fa";
_sql.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"database.db",anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 36;BA.debugLine="drawer.Initialize(Me,\"\",Activity,60%x)";
mostCurrent._drawer._initialize /*String*/ (mostCurrent.activityBA,teacherform.getObject(),"",(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._activity.getObject())),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA));
 //BA.debugLineNum = 37;BA.debugLine="drawer.CenterPanel.LoadLayout(\"teacherform\")";
mostCurrent._drawer._getcenterpanel /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().LoadLayout("teacherform",mostCurrent.activityBA);
 //BA.debugLineNum = 38;BA.debugLine="drawer.Leftpanel.Loadlayout(\"laydrawer\")";
mostCurrent._drawer._getleftpanel /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().LoadLayout("laydrawer",mostCurrent.activityBA);
 //BA.debugLineNum = 39;BA.debugLine="lblme.Text = Main.myfname&\" \"&Main.mylname";
mostCurrent._lblme.setText(BA.ObjectToCharSequence(mostCurrent._main._myfname /*String*/ +" "+mostCurrent._main._mylname /*String*/ ));
 //BA.debugLineNum = 41;BA.debugLine="B4XTable1.AddColumn(\"Teacher\",B4XTable1.COLUMN_TY";
mostCurrent._b4xtable1._addcolumn /*b4a.example.b4xtable._b4xtablecolumn*/ ("Teacher",mostCurrent._b4xtable1._column_type_text /*int*/ );
 //BA.debugLineNum = 42;BA.debugLine="B4XTable1.AddColumn(\"Student\",B4XTable1.COLUMN_TY";
mostCurrent._b4xtable1._addcolumn /*b4a.example.b4xtable._b4xtablecolumn*/ ("Student",mostCurrent._b4xtable1._column_type_text /*int*/ );
 //BA.debugLineNum = 43;BA.debugLine="B4XTable1.AddColumn(\"Grades\",B4XTable1.COLUMN_TYP";
mostCurrent._b4xtable1._addcolumn /*b4a.example.b4xtable._b4xtablecolumn*/ ("Grades",mostCurrent._b4xtable1._column_type_numbers /*int*/ );
 //BA.debugLineNum = 44;BA.debugLine="showstudents";
_showstudents();
 //BA.debugLineNum = 48;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 54;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 56;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 50;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 52;BA.debugLine="End Sub";
return "";
}
public static String  _btnadd_click() throws Exception{
String _qry = "";
String _ans = "";
String _enum = "";
anywheresoftware.b4a.sql.SQL.CursorWrapper _c1 = null;
String _sname = "";
 //BA.debugLineNum = 64;BA.debugLine="Sub btnadd_Click";
 //BA.debugLineNum = 65;BA.debugLine="Dim qry As String";
_qry = "";
 //BA.debugLineNum = 66;BA.debugLine="Dim ans As String";
_ans = "";
 //BA.debugLineNum = 67;BA.debugLine="Dim enum As String";
_enum = "";
 //BA.debugLineNum = 68;BA.debugLine="Dim c1 As Cursor";
_c1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 69;BA.debugLine="Dim sname As String";
_sname = "";
 //BA.debugLineNum = 71;BA.debugLine="inpdlg.input = \"\"";
mostCurrent._inpdlg.setInput("");
 //BA.debugLineNum = 72;BA.debugLine="ans = inpdlg.Show(\"Enter User Number\",\"Update Use";
_ans = BA.NumberToString(mostCurrent._inpdlg.Show("Enter User Number","Update User","Ok","Cancel","",mostCurrent.activityBA,(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 73;BA.debugLine="enum = inpdlg.Input";
_enum = mostCurrent._inpdlg.getInput();
 //BA.debugLineNum = 74;BA.debugLine="qry = \"Select uname from users where uname = ?\"";
_qry = "Select uname from users where uname = ?";
 //BA.debugLineNum = 75;BA.debugLine="c1 = sql.ExecQuery2(qry, Array As String(enum))";
_c1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_sql.ExecQuery2(_qry,new String[]{_enum})));
 //BA.debugLineNum = 76;BA.debugLine="If c1.RowCount > 0 Then";
if (_c1.getRowCount()>0) { 
 //BA.debugLineNum = 78;BA.debugLine="qry = \"Select student, teacher,grades from tblt";
_qry = "Select student, teacher,grades from tblteacherstud where student = ? and teacher = ?";
 //BA.debugLineNum = 79;BA.debugLine="c1 = sql.ExecQuery2(qry, Array As String(enum,M";
_c1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_sql.ExecQuery2(_qry,new String[]{_enum,mostCurrent._main._myuname /*String*/ })));
 //BA.debugLineNum = 80;BA.debugLine="If c1.RowCount <= 0 Then";
if (_c1.getRowCount()<=0) { 
 //BA.debugLineNum = 82;BA.debugLine="qry = \"Insert into tblteacherstud(student,tea";
_qry = "Insert into tblteacherstud(student,teacher,grades)values(?,?,?)";
 //BA.debugLineNum = 83;BA.debugLine="sql.ExecNonQuery2(qry, Array As String(enum,M";
_sql.ExecNonQuery2(_qry,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{_enum,mostCurrent._main._myuname /*String*/ ,BA.NumberToString(0)}));
 //BA.debugLineNum = 84;BA.debugLine="Msgbox(\"Student Successfully Added In your Cl";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Student Successfully Added In your Class"),BA.ObjectToCharSequence(""),mostCurrent.activityBA);
 //BA.debugLineNum = 85;BA.debugLine="showstudents";
_showstudents();
 }else {
 //BA.debugLineNum = 87;BA.debugLine="Msgbox(\"Student already in your class\",\"Error\"";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Student already in your class"),BA.ObjectToCharSequence("Error"),mostCurrent.activityBA);
 };
 }else {
 //BA.debugLineNum = 90;BA.debugLine="Msgbox(\"For More Info Contact Admin\",\"No Studen";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("For More Info Contact Admin"),BA.ObjectToCharSequence("No Student Enrolled"),mostCurrent.activityBA);
 };
 //BA.debugLineNum = 92;BA.debugLine="End Sub";
return "";
}
public static String  _btndelete_click() throws Exception{
String _ans = "";
String _enum = "";
String _qry = "";
anywheresoftware.b4a.sql.SQL.CursorWrapper _c1 = null;
String _x = "";
 //BA.debugLineNum = 151;BA.debugLine="Sub btndelete_Click";
 //BA.debugLineNum = 152;BA.debugLine="Dim ans As String";
_ans = "";
 //BA.debugLineNum = 153;BA.debugLine="Dim enum As String";
_enum = "";
 //BA.debugLineNum = 154;BA.debugLine="Dim qry As String";
_qry = "";
 //BA.debugLineNum = 155;BA.debugLine="Dim c1 As Cursor";
_c1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 157;BA.debugLine="inpdlg.Input=\"\"";
mostCurrent._inpdlg.setInput("");
 //BA.debugLineNum = 158;BA.debugLine="ans  = inpdlg.Show(\"Enter employee number\",\"Delet";
_ans = BA.NumberToString(mostCurrent._inpdlg.Show("Enter employee number","Delete Records","Ok","Cancel","",mostCurrent.activityBA,(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 159;BA.debugLine="enum = inpdlg.Input";
_enum = mostCurrent._inpdlg.getInput();
 //BA.debugLineNum = 160;BA.debugLine="If ans = DialogResponse.POSITIVE Then";
if ((_ans).equals(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE))) { 
 //BA.debugLineNum = 161;BA.debugLine="qry = \"Select student From tblteacherstud where";
_qry = "Select student From tblteacherstud where student = ?";
 //BA.debugLineNum = 162;BA.debugLine="c1 = sql.ExecQuery2(qry, Array As String(enum))";
_c1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_sql.ExecQuery2(_qry,new String[]{_enum})));
 //BA.debugLineNum = 163;BA.debugLine="If c1.RowCount > 0 Then";
if (_c1.getRowCount()>0) { 
 //BA.debugLineNum = 164;BA.debugLine="Dim x As String";
_x = "";
 //BA.debugLineNum = 165;BA.debugLine="x = Msgbox2(\"Drop this Student?\",\"Are you Sure?";
_x = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Drop this Student?"),BA.ObjectToCharSequence("Are you Sure?"),"Ok","Cancel","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 //BA.debugLineNum = 166;BA.debugLine="If x = DialogResponse.POSITIVE Then";
if ((_x).equals(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE))) { 
 //BA.debugLineNum = 167;BA.debugLine="qry = \"Delete from tblteacherstud where student";
_qry = "Delete from tblteacherstud where student = ? and teacher = ?";
 //BA.debugLineNum = 168;BA.debugLine="sql.ExecNonQuery2(qry, Array As String(enum,Ma";
_sql.ExecNonQuery2(_qry,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{_enum,mostCurrent._main._myuname /*String*/ }));
 //BA.debugLineNum = 169;BA.debugLine="Msgbox(\"Student Dropped\",\"\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Student Dropped"),BA.ObjectToCharSequence(""),mostCurrent.activityBA);
 //BA.debugLineNum = 170;BA.debugLine="showstudents";
_showstudents();
 }else {
 //BA.debugLineNum = 172;BA.debugLine="Msgbox(\"Deletion cancelled\",\"\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Deletion cancelled"),BA.ObjectToCharSequence(""),mostCurrent.activityBA);
 };
 }else {
 //BA.debugLineNum = 176;BA.debugLine="Msgbox(\"No record found\",\"\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("No record found"),BA.ObjectToCharSequence(""),mostCurrent.activityBA);
 //BA.debugLineNum = 177;BA.debugLine="showstudents";
_showstudents();
 };
 };
 //BA.debugLineNum = 180;BA.debugLine="End Sub";
return "";
}
public static String  _btnupdate_click() throws Exception{
String _ans2 = "";
String _enum2 = "";
String _qry2 = "";
anywheresoftware.b4a.sql.SQL.CursorWrapper _c2 = null;
String _ans = "";
String _enum = "";
String _qry = "";
anywheresoftware.b4a.sql.SQL.CursorWrapper _c1 = null;
 //BA.debugLineNum = 114;BA.debugLine="Sub btnupdate_Click";
 //BA.debugLineNum = 115;BA.debugLine="Dim ans2 As String";
_ans2 = "";
 //BA.debugLineNum = 116;BA.debugLine="Dim enum2 As String";
_enum2 = "";
 //BA.debugLineNum = 117;BA.debugLine="Dim qry2 As String";
_qry2 = "";
 //BA.debugLineNum = 118;BA.debugLine="Dim c2 As Cursor";
_c2 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 119;BA.debugLine="Dim ans As String";
_ans = "";
 //BA.debugLineNum = 120;BA.debugLine="Dim enum As String";
_enum = "";
 //BA.debugLineNum = 121;BA.debugLine="Dim qry As String";
_qry = "";
 //BA.debugLineNum = 122;BA.debugLine="Dim c1 As Cursor";
_c1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 124;BA.debugLine="inpdlg.Input=\"\"";
mostCurrent._inpdlg.setInput("");
 //BA.debugLineNum = 125;BA.debugLine="ans2 = inpdlg.Show(\"Enter Student Name\",\"Modify G";
_ans2 = BA.NumberToString(mostCurrent._inpdlg.Show("Enter Student Name","Modify Grades","Ok","Cancel","",mostCurrent.activityBA,(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 126;BA.debugLine="enum2 = inpdlg.INPUT";
_enum2 = mostCurrent._inpdlg.getInput();
 //BA.debugLineNum = 128;BA.debugLine="If ans2 = DialogResponse.POSITIVE Then";
if ((_ans2).equals(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE))) { 
 //BA.debugLineNum = 129;BA.debugLine="qry2 = \"Select student from tblteacherstud where";
_qry2 = "Select student from tblteacherstud where student = ?";
 //BA.debugLineNum = 130;BA.debugLine="c2 = sql.ExecQuery2(qry2, Array As String(enum2)";
_c2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(_sql.ExecQuery2(_qry2,new String[]{_enum2})));
 //BA.debugLineNum = 131;BA.debugLine="If c2.RowCount > 0 Then";
if (_c2.getRowCount()>0) { 
 //BA.debugLineNum = 134;BA.debugLine="inpdlg.Input=\"\"";
mostCurrent._inpdlg.setInput("");
 //BA.debugLineNum = 135;BA.debugLine="ans = inpdlg.Show(\"Enter Student Grades\",\"Modif";
_ans = BA.NumberToString(mostCurrent._inpdlg.Show("Enter Student Grades","Modify Grades","Ok","Cancel","",mostCurrent.activityBA,(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null)));
 //BA.debugLineNum = 136;BA.debugLine="enum = inpdlg.INPUT";
_enum = mostCurrent._inpdlg.getInput();
 //BA.debugLineNum = 137;BA.debugLine="If ans = DialogResponse.POSITIVE Then";
if ((_ans).equals(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE))) { 
 //BA.debugLineNum = 138;BA.debugLine="qry = \"Update tblteacherstud set grades = ? wh";
_qry = "Update tblteacherstud set grades = ? where student = ? and teacher = ?";
 //BA.debugLineNum = 139;BA.debugLine="sql.ExecNonQuery2(qry,Array As String(enum,enu";
_sql.ExecNonQuery2(_qry,anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{_enum,_enum2,mostCurrent._main._myuname /*String*/ }));
 //BA.debugLineNum = 140;BA.debugLine="showstudents";
_showstudents();
 //BA.debugLineNum = 141;BA.debugLine="Msgbox(\"User Successfully Updated\",\"\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("User Successfully Updated"),BA.ObjectToCharSequence(""),mostCurrent.activityBA);
 };
 }else {
 //BA.debugLineNum = 144;BA.debugLine="Msgbox(\"No Student Found\",\"\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("No Student Found"),BA.ObjectToCharSequence(""),mostCurrent.activityBA);
 };
 };
 //BA.debugLineNum = 149;BA.debugLine="End Sub";
return "";
}
public static String  _copydbifneeded(String _filename) throws Exception{
 //BA.debugLineNum = 58;BA.debugLine="Sub copydbifneeded(Filename As String)";
 //BA.debugLineNum = 59;BA.debugLine="If File.Exists(File.DirInternal,Filename) = False";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_filename)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 60;BA.debugLine="File.Copy(File.DirAssets,Filename,File.DirIntern";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),_filename,anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_filename);
 };
 //BA.debugLineNum = 62;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 12;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 15;BA.debugLine="Private etsname As EditText";
mostCurrent._etsname = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Private B4XTable1 As B4XTable";
mostCurrent._b4xtable1 = new b4a.example.b4xtable();
 //BA.debugLineNum = 17;BA.debugLine="Private btnadd As Button";
mostCurrent._btnadd = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Private btnupdate As Button";
mostCurrent._btnupdate = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Dim inpdlg As InputDialog";
mostCurrent._inpdlg = new anywheresoftware.b4a.agraham.dialogs.InputDialog();
 //BA.debugLineNum = 20;BA.debugLine="Private etsname As EditText";
mostCurrent._etsname = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Private drawer As B4XDrawer";
mostCurrent._drawer = new b4a.example.b4xdrawer();
 //BA.debugLineNum = 22;BA.debugLine="Private Label2 As Label";
mostCurrent._label2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private lblabout As Label";
mostCurrent._lblabout = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private lblhome As Label";
mostCurrent._lblhome = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private lbllogout As Label";
mostCurrent._lbllogout = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private lblme As Label";
mostCurrent._lblme = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 27;BA.debugLine="End Sub";
return "";
}
public static String  _label2_click() throws Exception{
 //BA.debugLineNum = 182;BA.debugLine="Sub Label2_Click";
 //BA.debugLineNum = 183;BA.debugLine="drawer.LeftOpen = True";
mostCurrent._drawer._setleftopen /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 184;BA.debugLine="End Sub";
return "";
}
public static String  _lblabout_click() throws Exception{
 //BA.debugLineNum = 205;BA.debugLine="Sub lblabout_Click";
 //BA.debugLineNum = 206;BA.debugLine="drawer.LeftOpen = False";
mostCurrent._drawer._setleftopen /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 207;BA.debugLine="StartActivity(\"about\")";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)("about"));
 //BA.debugLineNum = 208;BA.debugLine="End Sub";
return "";
}
public static String  _lblhome_click() throws Exception{
 //BA.debugLineNum = 201;BA.debugLine="Sub lblhome_Click";
 //BA.debugLineNum = 202;BA.debugLine="drawer.LeftOpen = False";
mostCurrent._drawer._setleftopen /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 203;BA.debugLine="End Sub";
return "";
}
public static String  _lbllogout_click() throws Exception{
int _msg = 0;
 //BA.debugLineNum = 192;BA.debugLine="Sub lbllogout_Click";
 //BA.debugLineNum = 193;BA.debugLine="Dim msg As Int";
_msg = 0;
 //BA.debugLineNum = 194;BA.debugLine="msg = Msgbox2(\"Do You Want To Logout?\",\"\",\"Yes\",\"";
_msg = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Do You Want To Logout?"),BA.ObjectToCharSequence(""),"Yes","No","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 195;BA.debugLine="If msg = DialogResponse.POSITIVE Then";
if (_msg==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 196;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 197;BA.debugLine="StartActivity(Main)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._main.getObject()));
 };
 //BA.debugLineNum = 199;BA.debugLine="End Sub";
return "";
}
public static String  _lblme_click() throws Exception{
 //BA.debugLineNum = 187;BA.debugLine="Sub lblme_Click";
 //BA.debugLineNum = 188;BA.debugLine="drawer.LeftOpen = False";
mostCurrent._drawer._setleftopen /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 189;BA.debugLine="StartActivity(\"profile\")";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)("profile"));
 //BA.debugLineNum = 190;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim sql As SQL";
_sql = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
public static String  _showstudents() throws Exception{
anywheresoftware.b4a.objects.collections.List _data = null;
String _qry = "";
anywheresoftware.b4a.sql.SQL.ResultSetWrapper _rs = null;
Object[] _row = null;
 //BA.debugLineNum = 94;BA.debugLine="Sub showstudents";
 //BA.debugLineNum = 95;BA.debugLine="Dim data As List";
_data = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 96;BA.debugLine="data.Initialize";
_data.Initialize();
 //BA.debugLineNum = 97;BA.debugLine="Dim qry As String";
_qry = "";
 //BA.debugLineNum = 98;BA.debugLine="qry = \"SELECT student,teacher,grades from tblteac";
_qry = "SELECT student,teacher,grades from tblteacherstud where teacher = ?";
 //BA.debugLineNum = 99;BA.debugLine="Dim rs As ResultSet = sql.ExecQuery2(qry, Array A";
_rs = new anywheresoftware.b4a.sql.SQL.ResultSetWrapper();
_rs = (anywheresoftware.b4a.sql.SQL.ResultSetWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.ResultSetWrapper(), (android.database.Cursor)(_sql.ExecQuery2(_qry,new String[]{mostCurrent._main._myuname /*String*/ })));
 //BA.debugLineNum = 100;BA.debugLine="Do While rs.NextRow";
while (_rs.NextRow()) {
 //BA.debugLineNum = 101;BA.debugLine="Dim row(3) As Object";
_row = new Object[(int) (3)];
{
int d0 = _row.length;
for (int i0 = 0;i0 < d0;i0++) {
_row[i0] = new Object();
}
}
;
 //BA.debugLineNum = 103;BA.debugLine="row(0) = rs.getstring(\"teacher\")";
_row[(int) (0)] = (Object)(_rs.GetString("teacher"));
 //BA.debugLineNum = 104;BA.debugLine="row(1) = rs.getstring(\"student\")";
_row[(int) (1)] = (Object)(_rs.GetString("student"));
 //BA.debugLineNum = 105;BA.debugLine="row(2) = rs.getstring(\"grades\")";
_row[(int) (2)] = (Object)(_rs.GetString("grades"));
 //BA.debugLineNum = 107;BA.debugLine="data.add(row)";
_data.Add((Object)(_row));
 }
;
 //BA.debugLineNum = 109;BA.debugLine="rs.Close";
_rs.Close();
 //BA.debugLineNum = 110;BA.debugLine="B4XTable1.SetData(data)";
mostCurrent._b4xtable1._setdata /*anywheresoftware.b4a.keywords.Common.ResumableSubWrapper*/ (_data);
 //BA.debugLineNum = 111;BA.debugLine="End Sub";
return "";
}
}
