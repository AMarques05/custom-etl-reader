import { useState } from "react";
import DownloadCSV from "./components/DownloadCSV";
import ImportCSVContainer from "./components/ImportCSVContainer";
import SelectColumns from "./components/SelectColumns";
import Options from "./components/Options";
import Table from "./components/Table";
import Submit from "./components/Submit";
import Filters from "./components/Filters";

export default function App() {
  const [newCSV, setNewCSV] = useState([]);
  const [csvData, setCsvData] = useState([]);
  const [selectedColumns, setSelectedColumns] = useState([]);
  const [selectedOptions, setSelectedOptions] = useState([]);
  const [filters, setFilters] = useState([]);
  const [isSubmitted, setIsSubmitted] = useState(false);

  const handleSubmit = async () => {
    const validFilters = filters.filter(filter => 
      filter.column && filter.operator && filter.value.trim()
    );

    const payload = {
      csvData: csvData,
      selectedColumns: selectedColumns,
      selectedOptions: selectedOptions,
      filters: validFilters
    };

    console.log("Sending to backend:", payload);
    
    try {
      const response = await fetch('/api/process', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
      const result = await response.json();
      setNewCSV(result);

      setIsSubmitted(true);
    } catch (error) {
      console.error("Error processing data:", error);
    }
  };

  if (isSubmitted) {
    const validFilters = filters.filter(filter => 
      filter.column && filter.operator && filter.value.trim()
    );

    return (
      <div className="bg-gray-700 min-h-screen py-8 px-4">
        <DownloadCSV newCSV={csvData} />
      </div>
    );
  }
  
  return (
    <div className="bg-gray-700 min-h-screen py-8 px-4">
      <div className="flex flex-col gap-6 bg-gray-500 p-6 rounded shadow-lg max-w-4xl mx-auto">
        <ImportCSVContainer onDataLoaded={setCsvData} />
        <Table csvData={csvData} />
        <SelectColumns 
          csvData={csvData} 
          selectedColumns={selectedColumns}
          onColumnsChange={setSelectedColumns}
        />
        <Options 
          csvData={csvData}
          selectedOptions={selectedOptions} 
          onOptionsChange={setSelectedOptions}
        />
        <Filters 
          csvData={csvData}
          filters={filters}
          onFiltersChange={setFilters}
        />
        <Submit 
          csvData={csvData} 
          onSubmit={handleSubmit}
          isDisabled={csvData.length === 0}
        />
      </div>
    </div>
  );
}