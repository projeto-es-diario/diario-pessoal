import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ChartOptions, ChartType, ChartDataset } from 'chart.js';
import { NgChartsModule } from 'ng2-charts';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-mood-chart',
  standalone: true,
  imports: [CommonModule, NgChartsModule],
  templateUrl: './mood-chart.component.html',
  styleUrls: ['./mood-chart.component.css']
})
export class MoodChartComponent implements OnChanges {
  @Input() entries: any[] = [];

  public lineChartData: ChartDataset[] = [
    { data: [], label: 'Tendência de Humor' },
  ];
  public lineChartLabels: string[] = [];
  public lineChartOptions: ChartOptions = {
    responsive: true,
    scales: {
      y: {
        min: 1,
        max: 5,
        ticks: {
          stepSize: 1
        }
      }
    }
  };
  public lineChartType: ChartType = 'line';

  constructor() { }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['entries'] && this.entries) {
      this.updateChartData();
    }
  }

  updateChartData(): void {
    const sortedEntries = this.entries
      .filter(e => e.mood != null)
      .sort((a, b) => new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime());

    // Utiliza toLocaleDateString com a localidade 'pt-BR'
    this.lineChartLabels = sortedEntries.map(e => new Date(e.createdAt).toLocaleDateString('pt-BR'));
    this.lineChartData[0].data = sortedEntries.map(e => e.mood);
  }
}