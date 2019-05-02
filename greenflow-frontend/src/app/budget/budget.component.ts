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
  public actualBudgets : any[]
  public editingLimit : boolean[]
  public editingWarning : boolean[]
  public editedLimit : number[]
  public editedWarning : number []

  constructor(private http: HttpClient,globals : Globals, public dialog: MatDialog ) {
    this.serverURL = globals.getBaseUrl()
   }

  ngOnInit() {
    this.listActualBudgets()
    this.editingWarning=new Array()
    this.editingLimit=new Array()
    this.editedLimit = new Array()
    this.editedWarning = new Array()
  }

  addBudget() {
    const dialogRef = this.dialog.open(BudgetEditorDialog, {
      //height: '400px',
      width: '600px',
      data: { operation : "post" }
    });

    dialogRef.afterClosed().subscribe(result => {
      this.listActualBudgets()
    });
  }
  updateLimit(index,id){
    this.editingLimit[index]=false
    this.updateBudget(index,id, this.editedLimit[index],"limit/")
    this.actualBudgets[index].limit= this.editedLimit[index]
  }
  updateWarning(index,id){
    this.editingWarning[index]=false
    this.updateBudget(index,id,this.editedWarning[index],"warning/")
    this.actualBudgets[index].warning= this.editedWarning[index]
  }

  updateBudget(index: number,id : number, body,url){
    var token=  JSON.parse(localStorage.getItem("currentUser")).token
    const headers = new HttpHeaders()
            .set("Authorization",token)
            .append('Content-Type', 'application/json');
    
    return this.http.put(this.serverURL+"budget/"+url + id, body, {headers, observe : 'response',withCredentials : true})
    .subscribe (
      data =>{
      },
      error => {
        //Update locally if success
          console.log("Error"+error);
      });  
  }

  removeBudget(id){
    var token=  JSON.parse(localStorage.getItem("currentUser")).token
    const headers = new HttpHeaders()
            .set("Authorization",token)
            .append('Content-Type', 'application/json');
    
    return this.http.delete(this.serverURL+"budget/"+ id, {headers, observe : 'response',withCredentials : true})
    .subscribe (
      data =>{
        this.listActualBudgets()
      },
      error => {
        //Update locally if success
          console.log("Error"+error);
      });  
  }

  listActualBudgets(){
    var token=  JSON.parse(localStorage.getItem("currentUser")).token
    var year = new Date().getFullYear().toString()
    var month = (new Date().getMonth()+1).toString()
    const headers = new HttpHeaders()
            .set("Authorization",token)
            .append('Content-Type', 'application/json');
    return this.http.get<any>(this.serverURL+"budget",{headers, params: {year : year, month : month},observe : 'response',withCredentials : true})
    .subscribe (
      data =>{
        this.actualBudgets = <any> data.body
        let index=0
        this.actualBudgets.forEach((element:any)  => {
          this.editedLimit[index] =  element.limit
          this.editedWarning[index] =  element.limit
          this.editingLimit[index] = false
          this.editingWarning[index] = false
          index++;
          
        });
      },
      error => {
          console.log("Error"+error);
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
      warning: [],
      currency : [],
      paymentType :[]
    });
  }
  addBudget(budget){
    var token=  JSON.parse(localStorage.getItem("currentUser")).token
    const headers = new HttpHeaders()
            .set("Authorization",token)
            .append('Content-Type', 'application/json');
    return this.http.request<any>('post',this.serverURL+"budget",{headers,body : budget, observe : 'response',withCredentials : true})
    .subscribe (
      (data : any) =>{
      },
      error => {
          console.log("Error"+error);
      });
  }

}
