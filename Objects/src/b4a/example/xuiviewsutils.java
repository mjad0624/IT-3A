package b4a.example;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class xuiviewsutils {
private static xuiviewsutils mostCurrent = new xuiviewsutils();
public static Object getObject() {
    throw new RuntimeException("Code module does not support this method.");
}
 public anywheresoftware.b4a.keywords.Common __c = null;
public static boolean _utilsinitialized = false;
public static anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
public b4a.example.dateutils _dateutils = null;
public b4a.example.main _main = null;
public b4a.example.starter _starter = null;
public b4a.example.adminform _adminform = null;
public b4a.example.updateuser _updateuser = null;
public b4a.example.addusers _addusers = null;
public b4a.example.teacherform _teacherform = null;
public b4a.example.studentform _studentform = null;
public b4a.example.about _about = null;
public b4a.example.profile _profile = null;
public static String  _addstubtoclvifneeded(anywheresoftware.b4a.BA _ba,b4a.example3.customlistview _customlistview1,int _color) throws Exception{
b4a.example3.customlistview._clvitem _lastitem = null;
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
int _height = 0;
 //BA.debugLineNum = 34;BA.debugLine="Public Sub AddStubToCLVIfNeeded(CustomListView1 As";
 //BA.debugLineNum = 35;BA.debugLine="If CustomListView1.Size = 0 Then Return";
if (_customlistview1._getsize()==0) { 
if (true) return "";};
 //BA.debugLineNum = 36;BA.debugLine="Dim LastItem As CLVItem = CustomListView1.GetRawL";
_lastitem = _customlistview1._getrawlistitem((int) (_customlistview1._getsize()-1));
 //BA.debugLineNum = 37;BA.debugLine="If LastItem.Offset + LastItem.Panel.Height < Cust";
if (_lastitem.Offset+_lastitem.Panel.getHeight()<_customlistview1._asview().getHeight()) { 
 //BA.debugLineNum = 39;BA.debugLine="Dim p As B4XView = xui.CreatePanel(\"stub\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = _xui.CreatePanel((_ba.processBA == null ? _ba : _ba.processBA),"stub");
 //BA.debugLineNum = 40;BA.debugLine="p.Color = Color";
_p.setColor(_color);
 //BA.debugLineNum = 41;BA.debugLine="Dim Height As Int = CustomListView1.AsView.Heigh";
_height = (int) (_customlistview1._asview().getHeight()-_lastitem.Offset-_lastitem.Panel.getHeight()-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (3)));
 //BA.debugLineNum = 42;BA.debugLine="If xui.IsB4J Then Height = Height + 5";
if (_xui.getIsB4J()) { 
_height = (int) (_height+5);};
 //BA.debugLineNum = 43;BA.debugLine="p.SetLayoutAnimated(0, 0, 0, CustomListView1.AsV";
_p.SetLayoutAnimated((int) (0),(int) (0),(int) (0),_customlistview1._asview().getWidth(),_height);
 //BA.debugLineNum = 44;BA.debugLine="CustomListView1.Add(p, \"\")";
_customlistview1._add(_p,(Object)(""));
 //BA.debugLineNum = 45;BA.debugLine="CustomListView1.sv.ScrollViewContentHeight = Cus";
_customlistview1._sv.setScrollViewContentHeight((int) (_customlistview1._sv.getScrollViewContentHeight()-_customlistview1._getdividersize()));
 };
 //BA.debugLineNum = 47;BA.debugLine="End Sub";
return "";
}
public static String  _initialize(anywheresoftware.b4a.BA _ba) throws Exception{
 //BA.debugLineNum = 10;BA.debugLine="Private Sub Initialize";
 //BA.debugLineNum = 11;BA.debugLine="If UtilsInitialized Then Return";
if (_utilsinitialized) { 
if (true) return "";};
 //BA.debugLineNum = 12;BA.debugLine="UtilsInitialized = True";
_utilsinitialized = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return "";
}
public static String  _performhapticfeedback(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.B4XViewWrapper _view) throws Exception{
anywheresoftware.b4j.object.JavaObject _jo = null;
 //BA.debugLineNum = 21;BA.debugLine="Public Sub PerformHapticFeedback (View As B4XView)";
 //BA.debugLineNum = 22;BA.debugLine="Initialize";
_initialize(_ba);
 //BA.debugLineNum = 24;BA.debugLine="Dim jo As JavaObject = View";
_jo = new anywheresoftware.b4j.object.JavaObject();
_jo = (anywheresoftware.b4j.object.JavaObject) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4j.object.JavaObject(), (java.lang.Object)(_view.getObject()));
 //BA.debugLineNum = 25;BA.debugLine="jo.RunMethod(\"performHapticFeedback\", Array(1))";
_jo.RunMethod("performHapticFeedback",new Object[]{(Object)(1)});
 //BA.debugLineNum = 31;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 2;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 6;BA.debugLine="Private UtilsInitialized As Boolean";
_utilsinitialized = false;
 //BA.debugLineNum = 7;BA.debugLine="Private xui As XUI";
_xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 8;BA.debugLine="End Sub";
return "";
}
public static String  _setbitmapandfill(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.B4XViewWrapper _imageview,anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper _bmp) throws Exception{
anywheresoftware.b4a.objects.ImageViewWrapper _iiv = null;
 //BA.debugLineNum = 65;BA.debugLine="Public Sub SetBitmapAndFill (ImageView As B4XView,";
 //BA.debugLineNum = 66;BA.debugLine="ImageView.SetBitmap(Bmp)";
_imageview.SetBitmap((android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 67;BA.debugLine="Dim iiv As ImageView = ImageView";
_iiv = new anywheresoftware.b4a.objects.ImageViewWrapper();
_iiv = (anywheresoftware.b4a.objects.ImageViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ImageViewWrapper(), (android.widget.ImageView)(_imageview.getObject()));
 //BA.debugLineNum = 69;BA.debugLine="iiv.Gravity = Gravity.FILL";
_iiv.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 75;BA.debugLine="End Sub";
return "";
}
public static String  _settextorcsbuildertolabel(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.B4XViewWrapper _xlbl,Object _text) throws Exception{
 //BA.debugLineNum = 50;BA.debugLine="Public Sub SetTextOrCSBuilderToLabel(xlbl As B4XVi";
 //BA.debugLineNum = 52;BA.debugLine="xlbl.Text = Text";
_xlbl.setText(BA.ObjectToCharSequence(_text));
 //BA.debugLineNum = 62;BA.debugLine="End Sub";
return "";
}
}