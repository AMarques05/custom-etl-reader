import {useState} from "react";

function Filters(props) {
    const [filters, setFilters] = useState([
        {column: "", operator: "", value: ""}
    ]);

    function addFilter() {
        setFilters([...filters, {column: "", operator: "", value: ""}]);
    }

    const updateFilter = (index, key, value) => {
        const newFilters = [...filters];
        newFilters[index][key] = value;
        setFilters(newFilters);

        const validFilters = newFilters.filter(filter => 
            filter.column && filter.operator && filter.value.trim()
        );
        props.onFiltersChange(validFilters);
    };

    const removeFilter = (index) => {
        const newFilters = filters.filter((_, i) => i !== index);
        setFilters(newFilters);

        const validFilters = newFilters.filter(filter => 
            filter.column && filter.operator && filter.value.trim()
        );
        props.onFiltersChange(validFilters);
    };

    if(props.csvData.length === 0) {
        return (
        <div className="flex justify-center items-center rounded shadow-xl bg-gray-400 p-8 min-h-[100px]">
            <h1 className="text-xl">No data available, Please upload a CSV file</h1>
        </div>  
        );
    }

    return (
        <div className="bg-gray-400 p-6 rounded shadow-xl">
            <h2 className="text-xl font-bold mb-4">Filters</h2>
            {filters.map((filter, index) => (
                <div key={index} className="flex items-center gap-4 mb-2">
                    {/*Column Dropdown*/}
                    <select
                        value={filter.column}
                        onChange={(e) => updateFilter(index, "column", e.target.value)}
                        className="px-2 py-1 rounded border bg-gray-300 text-black focus:outline-none focus:ring-2 focus:ring-blue-500 transition-colors"
                    >
                    <option value="">Select Column</option>
                    {props.csvData.length > 0 && Object.keys(props.csvData[0]).map((col) => (
                        <option key={col} value={col}>{col}</option>
                    ))}
                    </select>

                    <select
                        value={filter.operator}
                        onChange={(e) => updateFilter(index, "operator", e.target.value)}
                        className="px-2 py-1 rounded border bg-gray-300 text-black focus:outline-none focus:ring-2 focus:ring-blue-500 transition-colors"
                    >
                        <option value="">Select Operator</option>
                        <option value="equals">Equals</option>
                        <option value="not_equals">Not Equals</option>
                        <option value="greater_than">Greater Than</option>
                        <option value="less_than">Less Than</option>
                        <option value="greater_than_equals">Greater Than / Equals To</option>
                        <option value="less_than_equals">Less Than / Equals To</option>
                        <option value="contains">Contains</option>
                    </select>

                    <input
                        type="text"
                        value={filter.value}
                        onChange={(e) => updateFilter(index, "value", e.target.value)}
                        className="px-2 py-1 rounded border bg-gray-300 text-black focus:outline-none focus:ring-2 focus:ring-blue-500 transition-colors"
                        placeholder="Filter Value"
                    />

                    {/* Remove filter button */}
                    <button
                        type="button"
                        onClick={() => removeFilter(index)}
                        className="px-2 py-1 rounded border bg-red-500 text-white transform hover:scale-105 transition duration-200"
                    >
                        <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                        </svg>
                    </button>
                </div>
            ))}

            <button
                type="button"
                onClick={addFilter}
                className="mt-4 px-2 py-1 rounded border bg-blue-500 text-white transform hover:scale-105 transition duration-200"
            >
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                </svg>
            </button>
        </div>
    );

}

export default Filters;