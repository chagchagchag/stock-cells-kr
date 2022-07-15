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
	  { make: 'Toyota', model: 'Celica', price: 35000 },
	  { make: 'Ford', model: 'Mondeo', price: 32000 },
	  { make: 'Porsche', model: 'Boxster', price: 72000 },
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
	  { field: 'make' },
	  { field: 'model' },
	  { field: 'price' },
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
				<button onClick={onBtnUpdate}>Show CSV export content text</button>
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