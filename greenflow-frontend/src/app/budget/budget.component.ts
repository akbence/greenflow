import { Component, OnInit,Inject } from '@angular/core';
import { Globals } from '../globals';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { FormGroup, FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-budget',
  templateUrl: './budget.component.html'
})
export class BudgetComponent implements OnInit {

  private username : string
  private registrationDate : Date
  public serverURL : string;

  constructor(private http: HttpClient,globals : Globals, public dialog: MatDialog ) {
    this.serverURL = globals.getBaseUrl()
   }

  ngOnInit() {
  }

  addBudget() {
    console.log("click")
    const dialogRef = this.dialog.open(BudgetEditorDialog, {
      //height: '400px',
      width: '600px',
      data: { operation : "post" }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log("dialog closed")
    });
  }
}

///BUDGET EDITOR DIALOG///
@Component({
  selector: 'budget-editor-dialog',
  templateUrl: './budget.editor.dialog.html',
})
export class BudgetEditorDialog {
  form: FormGroup
  serverURL : string
  operation : string

  constructor(private http: HttpClient,globals : Globals,
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<BudgetEditorDialog>,
    @Inject(MAT_DIALOG_DATA) public data: any) {
      this.serverURL = globals.getBaseUrl()
      this.operation = data.operation
    }

  onNoClick(): void {
    this.dialogRef.close();
  }
  save() {
    console.log(this.form.value)
    if(this.operation =="post"){
      this.addBudget(this.form.value)
    }
    this.dialogRef.close(this.form.value);
}
  close() {
      this.dialogRef.close();
}

  ngOnInit() {
    this.form = this.fb.group({
      limit : [],
      currency : [],
      paymentType :[]
    });
}
addBudget(budget){
  var token=  JSON.parse(localStorage.getItem("currentUser")).token
  const headers = new HttpHeaders()
          .set("Authorization",token)
          .append('Content-Type', 'application/json');
  return this.http.request<any>('post',this.serverURL+"budget",{headers,body : budget, observe : 'response'})
  .subscribe (
    (data : any) =>{
    },
    error => {
        console.log("Error"+error);
    });
}

}
