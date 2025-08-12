import {useMemo, useState} from "react";

function SelectColumns(props) {
    const [selectedColumns, setSelectedColumns] = useState([]);

    const columns = useMemo(() => {
        const set = new Set();
        for (const row of props.csvData) {
            for (const key in row) {
                set.add(key);
            }
        }
        return Array.from(set);
    }, [props.csvData]);


    if(props.csvData.length === 0) {
        return <div className="flex justify-center items-center rounded shadow-xl bg-gray-400 p-8 min-h-[200px]">
            <h1 className="text-xl">No data available, Please upload a CSV file</h1>
        </div>;
    }

    return (
        <div className="bg-gray-400 py-2 px-4 rounded shadow-xl max-h-[200px] flex gap-4">
            <h2 className="text-xl font-semibold mb-4">Select Columns: </h2>
            {/* Column selection functionality will go here */}
                {columns.map((column, index) => (
                    <button 
                        key={index}
                        onClick={() => {
                            const newSelectedColumns = selectedColumns.includes(column)
                                ? selectedColumns.filter(col => col !== column)
                                : [...selectedColumns, column];
                            setSelectedColumns(newSelectedColumns);
                            console.log("Selected columns:", newSelectedColumns);
                        }}
                        className={`px-3 py-1 rounded-full ${selectedColumns.includes(column) ? 'bg-blue-500 text-white' : 'bg-gray-300'} hover:bg-blue-300 hover:scale-105 transition-transform duration-200`}
                    >
                        {column}
                    </button>
                ))}
        </div>
    );
}

export default SelectColumns;