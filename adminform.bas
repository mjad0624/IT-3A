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
	Dim inpdlg As InputDialog
	Dim drawer As B4XDrawer
	
	Private lblme As Label
End Sub

Sub Activity_Create(FirstTime As Boolean)
	If FirstTime Then
		copydbifneeded("database.db")
		sql.Initialize(File.DirInternal,"database.db",False)
	End If
'	Activity.LoadLayout("adminform")
	drawer.Initialize(Me,"",Activity,60%x)
	drawer.CenterPanel.LoadLayout("adminform")
	drawer.Leftpanel.Loadlayout("laydrawer")
	lblme.Text = Main.myfname&" "&Main.mylname
	
	
	B4XTable1.AddColumn("ID",B4XTable1.COLUMN_TYPE_NUMBERS)
	B4XTable1.AddColumn("USERNAME",B4XTable1.COLUMN_TYPE_TEXT)
	B4XTable1.AddColumn("PASSWORD",B4XTable1.COLUMN_TYPE_TEXT)
	B4XTable1.AddColumn("FIRSTNAME",B4XTable1.COLUMN_TYPE_TEXT)
	B4XTable1.AddColumn("LASTNAME",B4XTable1.COLUMN_TYPE_TEXT)
	B4XTable1.AddColumn("ACCESS LEVEL",B4XTable1.COLUMN_TYPE_NUMBERS)
	B4XTable1.AddColumn("CLASS",B4XTable1.COLUMN_TYPE_TEXT)
	showuser


End Sub

Sub Activity_Resume
	showuser
End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub showuser
	Dim data As List
	data.Initialize
	Dim rs As ResultSet = sql.ExecQuery("Select users.id, users.fname, users.lname, users.uname, users.password, users.level,level.class from users inner join level where users.level = level.id")
	Do While rs.NextRow
		Dim row(7) As Object
		
		row(0) = rs.getstring("id")
		row(1) = rs.getstring("uname")
		row(2) = rs.getstring("password")
		row(3) = rs.getstring("fname")
		row(4) = rs.getstring("lname")
		row(5) = rs.getstring("level")
		row(6) = rs.getstring("class")
		data.add(row)
	Loop
	rs.Close
	B4XTable1.SetData(data)
End Sub

Sub copydbifneeded(Filename As String)
	If File.Exists(File.DirInternal,Filename) = False Then
		File.Copy(File.DirAssets,Filename,File.DirInternal,Filename)
	End If
End Sub



Sub btnupdate_Click
	Dim ans As String
	Dim enum As String
	Dim qry As String
	Dim c1 As Cursor

	inpdlg.input = ""
	ans = inpdlg.Show("Enter User Number","Update User","Ok","Cancel","",Null)
	enum = inpdlg.Input
	If ans = DialogResponse.POSITIVE Then
		qry = "Select * FROM users where id = ?"
		c1 = sql.ExecQuery2(qry, Array As String(enum))
		If c1.RowCount > 0 Then
			c1.Position = 0
			updateuser.ulevel = c1.GetString("level")
			updateuser.uname = c1.GetString("uname")
			updateuser.upass = c1.GetString("password")
			updateuser.uid = c1.GetString("id")
			updateuser.ufname = c1.GetString("fname")
			updateuser.ulname = c1.GetString("lname")
			StartActivity("updateuser")
		End If
	End If
End Sub

Sub Label2_Click
	drawer.LeftOpen = True
End Sub

Sub btndel_Click
	Dim ans As String
	Dim enum As String
	Dim qry As String
	Dim c1 As Cursor
	
	inpdlg.input = ""
	ans = inpdlg.Show("Enter User Number","Delete User","Ok","Cancel","",Null)
	enum = inpdlg.Input
	If ans = DialogResponse.POSITIVE Then
		qry = "Select * From users where id = ?"
		c1 = sql.ExecQuery2(qry, Array As String(enum))
		If c1.RowCount > 0 Then
			Dim x As String
			x = Msgbox2("Delete this record?","Deleting","Ok","Cancel","",Null)
			If x = DialogResponse.POSITIVE Then
				qry = "Delete from users where id = ?"
				sql.ExecNonQuery2(qry, Array As String(enum))
				showuser
			Else
				Msgbox("Deletion cancelled","")

			End If
		Else
			Msgbox("No record found","")
			showuser
		End If
	End If
End Sub

Sub btnclose_Click
	Activity.Finish
End Sub

Sub btnadd_Click
	StartActivity("addusers")
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
