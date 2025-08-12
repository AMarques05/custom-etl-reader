import { useState } from "react";
import ImportCSVContainer from "./components/ImportCSVContainer";
import SelectColumns from "./components/SelectColumns";
import Options from "./components/Options";
import Table from "./components/Table";

export default function App() {
  const [csvData, setCsvData] = useState([]);

  return (
    <div className="bg-gray-700 min-h-screen py-8 px-4">
      <div className="flex flex-col gap-6 bg-gray-500 p-6 rounded shadow-lg max-w-4xl mx-auto">
        <ImportCSVContainer onDataLoaded={setCsvData} />
        <Table csvData={csvData} />
        <SelectColumns csvData={csvData} />
        <Options csvData={csvData} />
      </div>
    </div>
  );
}
