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
				// https://www.ag-grid.com/react-data-grid/column-headers/
				wrapHeaderText: true,  // 응? 이건 무슨 옵션이지?? 다시 찾아봐야 함 
				autoHeaderHight: true, // 너비를 넘어가면 Hight를 늘리게끔 조절
				flex: 1,
			},
			popupParent: document.body,
			// columnDefs: [
			// 	{ field: 'price' }, {field: 'changedRatio'}, { field: 'changedPrice' }, {field: 'chnagedPer'}
			// ],
			columnDefs:[
				{
					headerName: '가격리스트',
					children:[
						{headerName: '회사명', field: 'company', minWidth: 50, resizable: true},
						{headerName: '조회 가격 (2022/07/08)', minWidth: 120, field: 'price', resizable: true},
						{headerName: '등락율(+/-)', field: 'priceRatio', resizable: true},
						{headerName: '적용 가격', field: 'changedPrice', resizable: true},
						{headerName: '적용 PER', field: 'changedPer', resizable: true},
					]
				},
				{
					headerName: '실적, 분기 데이터',
					children:[
						{headerName: 'PER', field: 'per', resizable: true},
						{headerName: 'PES', field: 'eps', resizable: true},
						{headerName: '당좌비율', field: 'cash'},
						{headerName: '유보율', field: 'cashcash'} // 유보율 ...
					]
				}
			],
			rowData: [
				{company: '삼성전자', price: 53400, changedPrice: 53300, priceRatio: 99, changedPer: 5, per: 6, eps: 55555, cash: 11111, cashcash: 11111},
				{company: '삼성전자', price: 53400, changedPrice: 53300, priceRatio: 98, changedPer: 5, per: 6, eps: 55555, cash: 11111, cashcash: 11111},
				{company: '삼성전자', price: 53400, changedPrice: 53300, priceRatio: 97, changedPer: 5, per: 6, eps: 55555, cash: 11111, cashcash: 11111},
				{company: '삼성전자', price: 53400, changedPrice: 53300, priceRatio: 96, changedPer: 5, per: 6, eps: 55555, cash: 11111, cashcash: 11111},
				{company: '삼성전자', price: 53400, changedPrice: 53300, priceRatio: 95, changedPer: 5, per: 6, eps: 55555, cash: 11111, cashcash: 11111},
				// {price: 35000, changedRatio: 90, changedPrice: 35000*0.9, changedPer: 111}
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
						{/* <button onClick={() => this.onBtnUpdate()}>
							Show CSV export content text
						</button> */}
						<button onClick={() => this.onBtnExport()}>
							Download CSV export file
						</button>
					</div>

					<div style={{ flex: '1 1 0', position: 'relative' }}>
						<div id="gridContainer" style={{height: '500px', width: '100%'}}>
							<div style={{height: '100%', width: '100%'}} className="ag-theme-alpine-dark">
								<AgGridReact 
									defaultColDef={this.state.defaultColDef}
									suppressExcelExport={true}
									popupParent={this.state.popupParent}
									columnDefs={this.state.columnDefs}
									rowData={this.state.rowData}
									onGridReady={this.onGridReady}
								/>
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