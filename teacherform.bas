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
	Private etsname As EditText
	Private B4XTable1 As B4XTable
	Private btnadd As Button
	Private btnupdate As Button
	Dim inpdlg As InputDialog
	Private etsname As EditText
	Private drawer As B4XDrawer
	Private Label2 As Label
	Private lblabout As Label
	Private lblhome As Label
	Private lbllogout As Label
	Private lblme As Label
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	If FirstTime Then
		copydbifneeded("database.db")
		sql.Initialize(File.DirInternal,"database.db",False)
	End If
'	Activity.LoadLayout("teacherform")
	drawer.Initialize(Me,"",Activity,60%x)
	drawer.CenterPanel.LoadLayout("teacherform")
	drawer.Leftpanel.Loadlayout("laydrawer")
	lblme.Text = Main.myfname&" "&Main.mylname
    
	B4XTable1.AddColumn("Teacher",B4XTable1.COLUMN_TYPE_TEXT)
	B4XTable1.AddColumn("Student",B4XTable1.COLUMN_TYPE_TEXT)
	B4XTable1.AddColumn("Grades",B4XTable1.COLUMN_TYPE_NUMBERS)
	showstudents



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

Sub btnadd_Click
	Dim qry As String
	Dim ans As String
	Dim enum As String
	Dim c1 As Cursor
	Dim sname As String
	
	inpdlg.input = ""
	ans = inpdlg.Show("Enter User Number","Update User","Ok","Cancel","",Null)
	enum = inpdlg.Input
		qry = "Select uname from users where uname = ?"
		c1 = sql.ExecQuery2(qry, Array As String(enum))
		If c1.RowCount > 0 Then	
			
			qry = "Select student, teacher,grades from tblteacherstud where student = ? and teacher = ?"
			c1 = sql.ExecQuery2(qry, Array As String(enum,Main.myuname))
			If c1.RowCount <= 0 Then
				
					qry = "Insert into tblteacherstud(student,teacher,grades)values(?,?,?)"
					sql.ExecNonQuery2(qry, Array As String(enum,Main.myuname,0))
					Msgbox("Student Successfully Added In your Class","")
					showstudents
			Else
				Msgbox("Student already in your class","Error")
				End If
		Else
			Msgbox("For More Info Contact Admin","No Student Enrolled")
		End If
End Sub

Sub showstudents
	Dim data As List
	data.Initialize
	Dim qry As String
	qry = "SELECT student,teacher,grades from tblteacherstud where teacher = ?"
	Dim rs As ResultSet = sql.ExecQuery2(qry, Array As String(Main.myuname))
	Do While rs.NextRow
		Dim row(3) As Object
	
		row(0) = rs.getstring("teacher")
		row(1) = rs.getstring("student")
		row(2) = rs.getstring("grades")
		
		data.add(row)
	Loop
	rs.Close
	B4XTable1.SetData(data)
End Sub


Sub btnupdate_Click
	Dim ans2 As String
	Dim enum2 As String
	Dim qry2 As String
	Dim c2 As Cursor
	Dim ans As String
	Dim enum As String
	Dim qry As String
	Dim c1 As Cursor
	
	inpdlg.Input=""
	ans2 = inpdlg.Show("Enter Student Name","Modify Grades","Ok","Cancel","",Null)
	enum2 = inpdlg.INPUT
	
	If ans2 = DialogResponse.POSITIVE Then
		qry2 = "Select student from tblteacherstud where student = ?"
		c2 = sql.ExecQuery2(qry2, Array As String(enum2))
		If c2.RowCount > 0 Then
				
				
			inpdlg.Input=""
			ans = inpdlg.Show("Enter Student Grades","Modify Grades","Ok","Cancel","",Null)
			enum = inpdlg.INPUT
			If ans = DialogResponse.POSITIVE Then
				qry = "Update tblteacherstud set grades = ? where student = ? and teacher = ?"
				sql.ExecNonQuery2(qry,Array As String(enum,enum2,Main.myuname))
				showstudents
				Msgbox("User Successfully Updated","")
			End If
		Else
			Msgbox("No Student Found","")
			
		End If
		
	End If
End Sub

Sub btndelete_Click
	Dim ans As String
	Dim enum As String
	Dim qry As String
	Dim c1 As Cursor
	
	inpdlg.Input=""
	ans  = inpdlg.Show("Enter employee number","Delete Records","Ok","Cancel","",Null)
	enum = inpdlg.Input
	If ans = DialogResponse.POSITIVE Then
		qry = "Select student From tblteacherstud where student = ?"
		c1 = sql.ExecQuery2(qry, Array As String(enum))
		If c1.RowCount > 0 Then
			Dim x As String
			x = Msgbox2("Drop this Student?","Are you Sure?","Ok","Cancel","",Null)
			If x = DialogResponse.POSITIVE Then
			qry = "Delete from tblteacherstud where student = ? and teacher = ?"
				sql.ExecNonQuery2(qry, Array As String(enum,Main.myuname))
				Msgbox("Student Dropped","")
				showstudents
			Else
				Msgbox("Deletion cancelled","")

			End If
		Else
			Msgbox("No record found","")
			showstudents
		End If
	End If
End Sub

Sub Label2_Click
	drawer.LeftOpen = True
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