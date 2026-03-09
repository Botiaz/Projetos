function ProgressChart({ values = [] }) {
  const max = Math.max(...values, 1)

  return (
    <div className="chart">
      {values.map((value, index) => (
        <div key={`${value}-${index}`} className="chart-bar-wrapper">
          <div className="chart-bar" style={{ height: `${(value / max) * 100}%` }} />
          <span>{value}%</span>
        </div>
      ))}
    </div>
  )
}

export default ProgressChart
