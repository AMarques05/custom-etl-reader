import { useState } from "react";
import DownloadCSV from "./components/DownloadCSV";
import ImportCSVContainer from "./components/ImportCSVContainer";
import SelectColumns from "./components/SelectColumns";
import Options from "./components/Options";
import Table from "./components/Table";
import Submit from "./components/Submit";
import Filters from "./components/Filters";
import ProcessedDataTable from "./components/ProcessedDataTable";

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

    let processedData = csvData;

    try {
      // Step 1: Clean data if options selected
      if (selectedOptions && selectedOptions.length > 0) {
        console.log("Cleaning data with options:", selectedOptions);
        const cleanPayload = {
          csvData: processedData,
          selectedOptions: selectedOptions
        };
        
        const cleanResponse = await fetch('/api/process/clean', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(cleanPayload)
        });
        processedData = await cleanResponse.json();
        console.log("Data after cleaning:", processedData);
      }

      // Step 2: Filter data if filters/columns selected
      if ((validFilters && validFilters.length > 0) || (selectedColumns && selectedColumns.length > 0)) {
        console.log("Filtering data with filters:", validFilters, "columns:", selectedColumns);
        const filterPayload = {
          csvData: processedData,
          selectedColumns: selectedColumns,
          filters: validFilters
        };
        
        const filterResponse = await fetch('/api/process/filter', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(filterPayload)
        });
        processedData = await filterResponse.json();
        console.log("Data after filtering:", processedData);
      }

      setNewCSV(processedData);
      setIsSubmitted(true);
    } catch (error) {
      console.error("Error processing data:", error);
    }
  };

  if (isSubmitted) {
    return (
      <div className="bg-gray-700 min-h-screen py-8 px-4">
        <div className="flex flex-col gap-6 bg-gray-500 p-6 rounded shadow-lg max-w-6xl mx-auto">
          <ProcessedDataTable processedData={newCSV} />
        </div>
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