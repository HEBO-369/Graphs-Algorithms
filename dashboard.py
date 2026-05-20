import streamlit as st
import pandas as pd
import plotly.express as px
import plotly.graph_objects as go
import os

st.set_page_config(
    page_title="Graph Algorithms Benchmarking Dashboard",
    layout="wide",
    initial_sidebar_state="expanded"
)

st.markdown("""
    <style>
    .main-title {
        font-size: 38px;
        font-weight: bold;
        color: #1E3A8A;
        text-align: center;
        margin-bottom: 5px;
    }
    .subtitle {
        font-size: 18px;
        color: #4B5563;
        text-align: center;
        margin-bottom: 30px;
    }
    .section-header {
        font-size: 24px;
        font-weight: bold;
        color: #1E3A8A;
        border-bottom: 2px solid #3B82F6;
        padding-bottom: 5px;
        margin-top: 20px;
        margin-bottom: 15px;
    }
    .metric-box {
        background-color: #F3F4F6;
        padding: 15px;
        border-radius: 10px;
        text-align: center;
        box-shadow: 2px 2px 5px rgba(0,0,0,0.05);
    }
    </style>
""", unsafe_allow_html=True)

st.sidebar.title("Dashboard Controls")
st.sidebar.markdown("---")
metric_to_plot = st.sidebar.selectbox("Select Time Metric", ["Mean(ms)", "Median(ms)", "StdDev(ms)"])

st.sidebar.markdown("---")
st.sidebar.subheader("Environment Specifications")
cpu = st.sidebar.text_input("CPU", "Intel Core i7 13th ")
ram = st.sidebar.text_input("RAM", "16 GB")
os_name = st.sidebar.text_input("Operating System", "Ubuntu 22.04 LTS / Linux")
java_ver = st.sidebar.text_input("Java Version", "OpenJDK 25")

def load_csv(file_name):
    if os.path.exists(file_name):
        return pd.read_csv(file_name)
    return None

mst_files = {
    "Sparse_Prim": load_csv("MST_Sparse_Prim.csv"),
    "Sparse_Kruskal": load_csv("MST_Sparse_Kruskal.csv"),
    "Dense_Prim": load_csv("MST_Dense_Prim.csv"),
    "Dense_Kruskal": load_csv("MST_Dense_Kruskal.csv"),
    "Complete_Prim": load_csv("MST_Complete_Prim.csv"),
    "Complete_Kruskal": load_csv("MST_Complete_Kruskal.csv")
}

sssp_files = {
    "Sparse_Dijkstra": load_csv("SSSP_Sparse_Dijkstra.csv"),
    "Dense_Dijkstra": load_csv("SSSP_Dense_Dijkstra.csv"),
    "Complete_Dijkstra": load_csv("SSSP_Complete_Dijkstra.csv")
}

dag_files = {
    "Dijkstra": load_csv("SSSP_DAG_Dijkstra.csv"),
    "Acyclic": load_csv("SSSP_DAG_AcyclicSP.csv")
}

all_loaded = all(v is not None for v in mst_files.values()) and \
             all(v is not None for v in sssp_files.values()) and \
             all(v is not None for v in dag_files.values())

st.markdown("""<div class="main-title">CSE224: Graph Algorithms Benchmarking Report</div>""", unsafe_allow_html=True)
st.markdown("""<div class="subtitle">An Interactive Empirical Performance Analysis of MST & SSSP Algorithms</div>""", unsafe_allow_html=True)

if not all_loaded:
    st.warning("Some CSV files are missing in the current directory. Showing placeholder visualizer template.")
    sizes = [1000, 2000, 3000, 4000, 5000]
    dummy_data = {"Graph_Size": sizes, "Mean(ms)": [5.2, 12.4, 22.1, 35.8, 52.1], "Median(ms)": [5.0, 12.0, 22.0, 35.0, 52.0], "StdDev(ms)": [0.2, 0.5, 0.8, 1.1, 1.4]}
    for k in mst_files.keys():
        if mst_files[k] is None: mst_files[k] = pd.DataFrame(dummy_data)
    for k in sssp_files.keys():
        if sssp_files[k] is None: sssp_files[k] = pd.DataFrame(dummy_data)
    for k in dag_files.keys():
        if dag_files[k] is None:
            if "Acyclic" in k:
                dag_files[k] = pd.DataFrame({"Graph_Size": sizes, "Mean(ms)": [1.1, 2.3, 3.4, 4.6, 5.8], "Median(ms)": [1.0, 2.2, 3.3, 4.5, 5.7], "StdDev(ms)": [0.05, 0.1, 0.12, 0.15, 0.18]})
            else:
                dag_files[k] = pd.DataFrame(dummy_data)

tabs = st.tabs(["Overview", "Minimum Spanning Tree (MST)", "Shortest Paths (SSSP)", "DAG Optimization Analysis"])

with tabs[0]:
    st.markdown("""<div class="section-header">Project Overview & Hardware Environment</div>""", unsafe_allow_html=True)

    col1, col2, col3, col4 = st.columns(4)
    with col1: st.markdown(f"""<div class="metric-box"><b>CPU</b><br>{cpu}</div>""", unsafe_allow_html=True)
    with col2: st.markdown(f"""<div class="metric-box"><b>RAM</b><br>{ram}</div>""", unsafe_allow_html=True)
    with col3: st.markdown(f"""<div class="metric-box"><b>OS</b><br>{os_name}</div>""", unsafe_allow_html=True)
    with col4: st.markdown(f"""<div class="metric-box"><b>Java</b><br>{java_ver}</div>""", unsafe_allow_html=True)

    st.markdown(r"""
    ### Assignment Objectives
    This benchmarking suite evaluates the execution times of essential graph algorithms across multiple network topologies and edge densities.

    1. **Minimum Spanning Trees (MST):** Compares **Lazy Prim's** vs **Kruskal's** algorithms.
    2. **Single-Source Shortest Paths (SSSP):** Evaluates **Dijkstra's** on general topologies and compares it against the linear-time **DAG Shortest Path** algorithm.

    ### Graph Topologies Under Test
    * **Sparse Graph:** Undirected connected graph where $E \approx 5V$.
    * **Dense Graph:** Undirected connected graph where $E \approx 25\%$ of all possible edges.
    * **Complete Graph:** Fully connected graph where $E = \frac{V(V-1)}{2}$.
    * **Directed Acyclic Graph (DAG):** Directed network with no cycles where $E \approx 5V$.
    """)

with tabs[1]:
    st.markdown("""<div class="section-header">Prim's vs Kruskal's Empirical Comparison</div>""", unsafe_allow_html=True)

    densities = ["Sparse", "Dense", "Complete"]
    selected_density = st.selectbox("Choose Graph Density for MST", densities)

    df_prim = mst_files[f"{selected_density}_Prim"]
    df_kruskal = mst_files[f"{selected_density}_Kruskal"]

    fig_mst = go.Figure()
    fig_mst.add_trace(go.Scatter(x=df_prim["Graph_Size"], y=df_prim[metric_to_plot], mode='lines+markers', name="Prim's Algorithm", line=dict(color='#3B82F6', width=3)))
    fig_mst.add_trace(go.Scatter(x=df_kruskal["Graph_Size"], y=df_kruskal[metric_to_plot], mode='lines+markers', name="Kruskal's Algorithm", line=dict(color='#EF4444', width=3)))
    fig_mst.update_layout(title=f"MST Construction Time ({metric_to_plot}) on {selected_density} Graph", xaxis_title="Number of Vertices (V)", yaxis_title="Time (ms)", template="plotly_white")
    st.plotly_chart(fig_mst, use_container_width=True)

    st.markdown("### Comprehensive Side-by-Side Data Views")
    col1, col2 = st.columns(2)
    with col1:
        st.subheader("Prim's Algorithm Raw Data")
        st.dataframe(df_prim, use_container_width=True)
    with col2:
        st.subheader("Kruskal's Algorithm Raw Data")
        st.dataframe(df_kruskal, use_container_width=True)

with tabs[2]:
    st.markdown("""<div class="section-header">Dijkstra's Performance Across Topologies</div>""", unsafe_allow_html=True)

    fig_dijkstra = go.Figure()
    fig_dijkstra.add_trace(go.Scatter(x=sssp_files["Sparse_Dijkstra"]["Graph_Size"], y=sssp_files["Sparse_Dijkstra"][metric_to_plot], mode='lines+markers', name="Sparse Graph", line=dict(color='#10B981', width=3)))
    fig_dijkstra.add_trace(go.Scatter(x=sssp_files["Dense_Dijkstra"]["Graph_Size"], y=sssp_files["Dense_Dijkstra"][metric_to_plot], mode='lines+markers', name="Dense Graph", line=dict(color='#F59E0B', width=3)))
    fig_dijkstra.add_trace(go.Scatter(x=sssp_files["Complete_Dijkstra"]["Graph_Size"], y=sssp_files["Complete_Dijkstra"][metric_to_plot], mode='lines+markers', name="Complete Graph", line=dict(color='#8B5CF6', width=3)))
    fig_dijkstra.update_layout(title=f"Dijkstra's SSSP Scaling ({metric_to_plot})", xaxis_title="Number of Vertices (V)", yaxis_title="Time (ms)", template="plotly_white")
    st.plotly_chart(fig_dijkstra, use_container_width=True)

    st.markdown("### General Graphs SSSP Metrics Table")
    combined_sssp = pd.DataFrame({
        "Graph Size": sssp_files["Sparse_Dijkstra"]["Graph_Size"],
        "Sparse Dijkstra (ms)": sssp_files["Sparse_Dijkstra"][metric_to_plot],
        "Dense Dijkstra (ms)": sssp_files["Dense_Dijkstra"][metric_to_plot],
        "Complete Dijkstra (ms)": sssp_files["Complete_Dijkstra"][metric_to_plot]
    })
    st.dataframe(combined_sssp, use_container_width=True)

with tabs[3]:
    st.markdown("""<div class="section-header">Linear-Time DAG Algorithm vs Dijkstra's Algorithm</div>""", unsafe_allow_html=True)

    df_dijkstra_dag = dag_files["Dijkstra"]
    df_acyclic_dag = dag_files["Acyclic"]

    fig_dag = go.Figure()
    fig_dag.add_trace(go.Scatter(x=df_dijkstra_dag["Graph_Size"], y=df_dijkstra_dag[metric_to_plot], mode='lines+markers', name="Dijkstra's Algorithm", line=dict(color='#EF4444', width=3)))
    fig_dag.add_trace(go.Scatter(x=df_acyclic_dag["Graph_Size"], y=df_acyclic_dag[metric_to_plot], mode='lines+markers', name="DAG Linear Algorithm", line=dict(color='#10B981', width=3)))
    fig_dag.update_layout(title=f"SSSP Execution Comparison on DAG Topology ({metric_to_plot})", xaxis_title="Number of Vertices (V)", yaxis_title="Time (ms)", template="plotly_white")
    st.plotly_chart(fig_dag, use_container_width=True)

    st.markdown("### Algorithmic Speedup Factor Analysis")
    speedup_factors = df_dijkstra_dag["Mean(ms)"] / df_acyclic_dag["Mean(ms)"]
    df_speedup = pd.DataFrame({
        "Graph_Size": df_dijkstra_dag["Graph_Size"],
        "Dijkstra Mean (ms)": df_dijkstra_dag["Mean(ms)"],
        "DAG Linear Mean (ms)": df_acyclic_dag["Mean(ms)"],
        "Speedup Factor": speedup_factors
    })

    fig_speedup = px.bar(df_speedup, x="Graph_Size", y="Speedup Factor", text=df_speedup["Speedup Factor"].apply(lambda x: f"{x:.2f}x"), color="Speedup Factor", color_continuous_scale=px.colors.sequential.Viridis)
    fig_speedup.update_layout(title="Speedup Factor of Linear DAG Algorithm over Dijkstra", xaxis_title="Graph Size (V)", yaxis_title="Speedup Multiplier (X)", template="plotly_white")
    st.plotly_chart(fig_speedup, use_container_width=True)

    st.dataframe(df_speedup, use_container_width=True)