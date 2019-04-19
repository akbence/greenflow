import { Component, OnInit } from '@angular/core';
import { LocationStrategy, PlatformLocation, Location } from '@angular/common';
import { LegendItem, ChartType } from '../lbd/lbd-chart/lbd-chart.component';
import * as Chartist from 'chartist';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

    public pieChartLabels:string[] = ['Chrome', 'Safari', 'Firefox','Internet Explorer','Other'];
    public pieChartData:number[] = [60, 20, 20 , 10,10];
    public pieChartType:string = 'pie';

    public chartOptions = {
      maintainAspectRatio: false
   };

  constructor() { }

  ngOnInit() {

    } 

}
