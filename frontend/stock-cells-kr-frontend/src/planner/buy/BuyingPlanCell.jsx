import React, { Component } from 'react';
import { AgGridReact } from 'ag-grid-react';
import 'ag-grid-community/styles/ag-grid.css';
// import 'ag-grid-community/styles/ag-theme-alpine.css';
import './styles/styles-ag-grid.css';
import './styles/ag-theme-alpine-dark.css';

class BuyPlannerCell extends Component {

	constructor(props) {
		super(props);
	
		this.state = {
			defaultColDef: {
				editable: true,
				resizable: true,
				minWidth: 100,
				flex: 1,
			},
			popupParent: document.body,
			columnDefs: [
				{ field: 'price' }, {field: 'changedRatio'}, { field: 'changedPrice' }, {field: 'chnagedPer'}
			],
			rowData: [
				{price: 35000, changedRatio: 90, changedPrice: 35000*0.9, changedPer: 111}
			],
		};
	}
	
	onGridReady = (params) => {
		this.gridApi = params.api;
		this.gridColumnApi = params.columnApi;
	};
	
	onBtnExport = () => {
		this.gridApi.exportDataAsCsv();
	};
	
	onBtnUpdate = () => {
		document.querySelector('#csvResult').value = this.gridApi.getDataAsCsv();
	};

	render() {
		return (
			<div style={{width: '100%', height: '100%'}}>
				<div style={{ display: 'flex', flexDirection: 'column', height: '100%'}}>
					<div style={{ margin: '10px 0' }}>
						<button onClick={() => this.onBtnUpdate()}>
							Show CSV export content text
						</button>
						<button onClick={() => this.onBtnExport()}>
							Download CSV export file
						</button>
					</div>

					<div style={{ flex: '1 1 0', position: 'relative' }}>
						<div id="gridContainer" style={{height: '500px', width: '100%'}}>
							<div style={{height: '100%', width: '100%'}} className="ag-theme-alpine-dark">
							{/* <div style={{height: '100%'}} className="ag-theme-alpine"> */}
								<AgGridReact 
									defaultColDef={this.state.defaultColDef}
									suppressExcelExport={true}
									popupParent={this.state.popupParent}
									columnDefs={this.state.columnDefs}
									rowData={this.state.rowData}
									onGridReady={this.onGridReady}
								/>
								{/* <AgGridReact
									rowData={this.state.rowData}
									columnDefs={this.state.columnDefs}
								/> */}
							</div>
						</div>
						{/* <textarea id="csvResult" defaultValue={"Click the Show CSV export content button to view exported CSV here"}>
						</textarea> */}
					</div>
				</div>
			</div>
		);
	}
}

export default BuyPlannerCell;