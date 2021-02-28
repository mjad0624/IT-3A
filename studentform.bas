B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=10.2
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: True
	#IncludeTitle: False
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Dim sql As SQL

End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Private B4XTable1 As B4XTable
	Dim drawer As B4XDrawer
	Private lblme As Label
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	If FirstTime Then
		copydbifneeded("database.db")
		sql.Initialize(File.DirInternal,"database.db",False)
	End If
'	Activity.LoadLayout("studentform")
	drawer.Initialize(Me,"",Activity,60%x)
	drawer.CenterPanel.LoadLayout("studentform")
	drawer.Leftpanel.Loadlayout("laydrawer")
	lblme.Text = Main.myfname&" "&Main.mylname
	B4XTable1.AddColumn("Student",B4XTable1.COLUMN_TYPE_TEXT)
	B4XTable1.AddColumn("Teacher",B4XTable1.COLUMN_TYPE_TEXT)
	B4XTable1.AddColumn("Grades",B4XTable1.COLUMN_TYPE_NUMBERS)
		showgrades



End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub copydbifneeded(Filename As String)
	If File.Exists(File.DirInternal,Filename) = False Then
		File.Copy(File.DirAssets,Filename,File.DirInternal,Filename)
	End If
End Sub

Sub Label2_Click
	drawer.LeftOpen = True
End Sub

Sub showgrades
	Dim data As List
	data.Initialize
	Dim qry As String
	qry = "SELECT student,teacher,grades from tblteacherstud where student = ?"
	Dim rs As ResultSet = sql.ExecQuery2(qry, Array As String(Main.myuname))
	Do While rs.NextRow
		Dim row(3) As Object
		
		row(0) = rs.getstring("student")
		row(1) = rs.getstring("teacher")
		row(2) = rs.getstring("grades")
		
		data.add(row)
	Loop
	rs.Close
	B4XTable1.SetData(data)
End Sub

Sub lblme_Click
	drawer.LeftOpen = False
	StartActivity("profile")
End Sub

Sub lbllogout_Click
	Dim msg As Int
	msg = Msgbox2("Do You Want To Logout?","","Yes","No","",Null)
	If msg = DialogResponse.POSITIVE Then
		Activity.Finish
		StartActivity(Main)
	End If
End Sub

Sub lblhome_Click
	drawer.LeftOpen = False
End Sub

Sub lblabout_Click
	drawer.LeftOpen = False
	StartActivity("about")
End Sub