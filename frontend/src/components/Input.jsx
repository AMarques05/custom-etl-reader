export default function Input() {

	return (
		<input
			id="csv"
			type="file"
			className={
				"w-full rounded-md bg-gray-400 text-gray-100 placeholder-gray-300 " +
				"border border-gray-600 focus:border-cyan-400 outline-none " +
				"px-3 py-2 shadow-sm border-dashed"
			}
		/>
	);
}
