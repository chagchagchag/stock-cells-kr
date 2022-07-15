import React,{useState, useCallback, useMemo, useRef} from 'react'
import { AgGridReact } from 'ag-grid-react';
import 'ag-grid-community/styles/ag-grid.css';
import 'ag-grid-community/styles/ag-theme-alpine.css';
import './styles/styles-ag-grid.css';

const BuyPriceTable = () => {
	const gridRef = useRef();
	const containerStyle = useMemo(() => ({ width: '100%', height: '100%' }), []);
	const gridStyle = useMemo(() => ({ height: '100%', width: '100%' }), []);
	const [rowData, setRowData] = useState([
		{company: '삼성전자', price: 53400, changedPrice: 53400, priceRatio: 100, changedPer: 5, per: 6, eps: 55555, cash: 11111, cashcash: 11111},
		{company: '삼성전자', price: 53400, changedPrice: 53300, priceRatio: 99, changedPer: 4, per: 6, eps: 55555, cash: 11111, cashcash: 11111},
		{company: '삼성전자', price: 53400, changedPrice: 53200, priceRatio: 98, changedPer: 3, per: 6, eps: 55555, cash: 11111, cashcash: 11111},
		{company: '삼성전자', price: 53400, changedPrice: 53100, priceRatio: 97, changedPer: 2, per: 6, eps: 55555, cash: 11111, cashcash: 11111},
		{company: '삼성전자', price: 53400, changedPrice: 53000, priceRatio: 96, changedPer: 1, per: 6, eps: 55555, cash: 11111, cashcash: 11111},
		{company: '삼성전자', price: 53400, changedPrice: 52900, priceRatio: 95, changedPer: 0, per: 6, eps: 55555, cash: 11111, cashcash: 11111},
	]);

	const defaultColDef = useMemo(() => {
	  return {
		editable: true,
		resizable: true,
		minWidth: 100,
		flex: 1,
	  };
	}, []);

	const popupParent = useMemo(() => {
	  return document.body;
	}, []);

	const [columnDefs, setColumnDefs] = useState([
		{
			headerName: '',
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
				{headerName: 'EPS', field: 'eps', resizable: true}
			]
		}
	]);
  
	const onBtnExport = useCallback(() => {
	  gridRef.current.api.exportDataAsCsv();
	}, []);
  
	const onBtnUpdate = useCallback(() => {
	  document.querySelector(
		'#csvResult'
	  ).value = gridRef.current.api.getDataAsCsv();
	}, []);

	return (
		<div style={containerStyle}>
			<div style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
			<div style={{ margin: '10px 0' }}>
				{/* <button onClick={onBtnUpdate}>Show CSV export content text</button> */}
				<button onClick={onBtnExport}>Download CSV export file</button>
			</div>
			<div style={{ flex: '1 1 0', position: 'relative' }}>
				{/* gridContainer 에 height:100% 되게끔 하는 법 찾아볼 것. 100% 먹이면 테이블이 사라짐..... */}
				<div id="gridContainer" style={{height: '500px', width: '100%'}}>
				<div style={gridStyle} className="ag-theme-alpine">
					<AgGridReact
						ref={gridRef}
						rowData={rowData}
						defaultColDef={defaultColDef}
						suppressExcelExport={true}
						popupParent={popupParent}
						columnDefs={columnDefs}
					></AgGridReact>
				</div>
				</div>
				{/* <textarea id="csvResult">
				Click the Show CSV export content button to view exported CSV here
				</textarea> */}
			</div>
			</div>
		</div>
	)
};

export default BuyPriceTable