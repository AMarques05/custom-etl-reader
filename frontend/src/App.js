import ImportCSVContainer from "./components/ImportCSVContainer";
import SelectColumns from "./components/SelectColumns";
import Options from "./components/Options";
import Table from "./components/Table";

export default function App() {
  return (
    <div className="bg-gray-700 flex items-center justify-center h-screen">
      <div className="grid grid-cols-12 grid-rows-7 gap-4 shadow-cyan-500/50 bg-gray-500 p-6 rounded shadow-lg min-w-3/5 max-w-4/5 min-h-2/3 max-h-4/5">
        <ImportCSVContainer />
        <SelectColumns />
        <Options />
        <Table />
      </div>
    </div>
  );
}
