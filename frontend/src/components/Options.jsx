import Option from "./Option";
import { useState } from 'react';

function Options(props) {
    const [options, setOptions] = useState([
        { id: "remove-duplicates", title: "Remove Duplicates", description: "Remove duplicate rows from the dataset.", selected: false },
        { id: "fill-missing-values", title: "Fill Missing Values", description: "Fill missing values using various strategies.", selected: false },
        { id: "remove-empty-rows", title: "Remove Empty Rows", description: "Remove rows that are completely empty.", selected: false },
        { id: "trim-whitespace", title: "Trim Whitespace", description: "Trim whitespace from the beginning and end of strings.", selected: false },
        { id: "normalize-text", title: "Normalize Text", description: "Normalize text by converting to lowercase and removing special characters.", selected: false },
        { id: "fix-date-format", title: "Fix Date Format", description: "Fix date format inconsistencies in the dataset.", selected: false },
    ]);

    if(props.csvData.length === 0) {
        return <div className="flex justify-center items-center rounded shadow-xl bg-gray-400 p-8 min-h-[200px]">
            <h1 className="text-xl">Upload CSV to see options</h1>
        </div>;
    }

    // Handle individual option selection
    function handleOptionToggle(optionId) {
        setOptions(prevOptions =>
            prevOptions.map(option =>
                option.id === optionId ? { ...option, selected: !option.selected } : option
            )
        );
    }

    // Handle select all/deselect all
    function handleSelectAll() {
        const allSelected = options.every(option => option.selected);
        setOptions(prevOptions =>
            prevOptions.map(option => ({ ...option, selected: !allSelected }))
        );
    }

    const allSelected = options.every(option => option.selected);

    return (
        <div className="bg-gray-400 p-6 rounded shadow-xl min-h-[200px]">
            <div className="flex flex-row items-center gap-4 w-full mb-4">
                <h2 className="text-xl font-semibold">Data Cleaning Options</h2>
                <h3 className="text-lg font-semibold">Select All:</h3>
                <button
                    onClick={handleSelectAll}
                    className="w-6 h-6 border-2 border-gray-500 rounded-full flex items-center justify-center focus:outline-none hover:border-blue-500 transition-colors"
                >
                    {allSelected && (
                        <span className="w-3 h-3 bg-blue-500 rounded-full"></span>
                    )}
                </button>
            </div>

            <div className="grid grid-cols-2 grid-rows-3 gap-4">
                {options.map(option => (
                    <Option 
                        key={option.id}
                        id={option.id}
                        title={option.title} 
                        description={option.description}
                        selected={option.selected}
                        onToggle={handleOptionToggle}
                    />
                ))}
            </div>
        </div>
    );
}

export default Options;