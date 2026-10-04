import { useEffect, useState } from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  LabelList,
} from "recharts";
import sponsorService from "./services/sponsorService";
import activityService from "./services/activityService";
import "./App.css";

function App() {
  const [stats, setStats] = useState(null);
  const [sponsors, setSponsors] = useState([]);
  const [pipelineStats, setPipelineStats] = useState([]);
  const [industryCounts, setIndustryCounts] = useState({});
  const [upcomingFollowUps, setUpcomingFollowUps] = useState([]);
  const [draggedSponsor, setDraggedSponsor] = useState(null);
  const [selectedSponsor, setSelectedSponsor] = useState(null);
  const [activities, setActivities] = useState([]);
  const [editingActivity, setEditingActivity] = useState(null);
  const [showActivityForm, setShowActivityForm] = useState(false);

  const [newActivity, setNewActivity] = useState({
    type: "CALL",
    description: "",
    activityDate: new Date().toISOString().split("T")[0],
    nextFollowUpDate: "",
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [companySearch, setCompanySearch] = useState("");
  const [statusFilter, setStatusFilter] = useState("");
  const [industryFilter, setIndustryFilter] = useState("");
  const [sortField, setSortField] = useState("");
  const [sortDirection, setSortDirection] = useState("asc");

  const [currentPage, setCurrentPage] = useState(1);
  const sponsorsPerPage = 10;
  const [editingSponsor, setEditingSponsor] = useState(null);
  const [viewingSponsor, setViewingSponsor] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [formLoading, setFormLoading] = useState(false);

  const [newSponsor, setNewSponsor] = useState({
    companyName: "",
    contactPerson: "",
    designation: "",
    email: "",
    phone: "",
    industry: "",
    notes: "",
    status: "LEAD",
  });

  // Load dashboard statistics
  const fetchStats = async () => {
    try {
      const data = await sponsorService.getDashboardStats();
      setStats(data);
    } catch (err) {
      console.error("Stats API Error:", err);
      setError("Failed to load dashboard statistics.");
    }
  };
  const fetchPipelineStats = async () => {
    try {
      const data = await sponsorService.getPipelineStats();

      console.log("Pipeline API Data:", data);

      setPipelineStats(data);
    } catch (err) {
      console.error("Pipeline API Error:", err);

      setError("Failed to load pipeline statistics.");
    }
  };

  const fetchUpcomingFollowUps = async () => {
    try {
      const data = await activityService.getUpcomingFollowUps();

      console.log("Upcoming Follow-ups API Data:", data);

      setUpcomingFollowUps(data);
    } catch (err) {
      console.error("Upcoming Follow-ups API Error:", err);

      setUpcomingFollowUps([]);
    }
  };

  const isFollowUpOverdue = (date) => {
    const today = new Date().toISOString().split("T")[0];

    return date < today;
  };

  const fetchIndustryCounts = async () => {
    try {
      const data = await sponsorService.getIndustryCounts();

      console.log("Industry API Data:", data);

      setIndustryCounts(data);
    } catch (err) {
      console.error("Industry API Error:", err);

      setError("Failed to load industry statistics.");
    }
  };

  // Load all sponsors
  const fetchSponsors = async () => {
    try {
      const data = await sponsorService.getAllSponsors();
      setSponsors(data);
    } catch (err) {
      console.error("Sponsors API Error:", err);
      setError("Failed to load sponsors.");
    }
  };

  const fetchActivities = async (sponsorId) => {
    try {
      const data = await activityService.getActivitiesBySponsor(sponsorId);

      console.log("Activity API Data:", data);

      setActivities(data);
    } catch (err) {
      console.error("Activity API Error:", err);

      setActivities([]);
    }
  };

  const handleCreateActivity = async () => {
    if (!selectedSponsor) {
      return;
    }

    if (!newActivity.description.trim()) {
      return;
    }

    try {
      const activity = {
        sponsorId: selectedSponsor.id,
        type: newActivity.type,
        description: newActivity.description,
        activityDate: newActivity.activityDate,
        nextFollowUpDate: newActivity.nextFollowUpDate || null,
      };

      const createdActivity = await activityService.createActivity(activity);

      console.log("Created Activity:", createdActivity);

      setActivities((currentActivities) => [
        createdActivity,
        ...currentActivities,
      ]);

      setNewActivity({
        type: "CALL",
        description: "",
        activityDate: new Date().toISOString().split("T")[0],
        nextFollowUpDate: "",
      });

      setShowActivityForm(false);
    } catch (err) {
      console.error("Create Activity Error:", err);
    }
  };

  const handleDeleteActivity = async (activityId) => {
    try {
      await activityService.deleteActivity(activityId);

      setActivities((currentActivities) =>
        currentActivities.filter((activity) => activity.id !== activityId),
      );
    } catch (err) {
      console.error("Delete Activity Error:", err);
    }
  };

  const handleUpdateActivity = async () => {
    if (!editingActivity || !selectedSponsor) {
      return;
    }

    if (!newActivity.description.trim()) {
      return;
    }

    try {
      const activity = {
        sponsorId: selectedSponsor.id,
        type: newActivity.type,
        description: newActivity.description,
        activityDate: newActivity.activityDate,
        nextFollowUpDate: newActivity.nextFollowUpDate || null,
      };

      const updatedActivity = await activityService.updateActivity(
        editingActivity.id,
        activity,
      );

      console.log("Updated Activity:", updatedActivity);

      setActivities((currentActivities) =>
        currentActivities.map((activity) =>
          activity.id === updatedActivity.id ? updatedActivity : activity,
        ),
      );

      setNewActivity({
        type: "CALL",
        description: "",
        activityDate: new Date().toISOString().split("T")[0],
        nextFollowUpDate: "",
      });

      setEditingActivity(null);
      setShowActivityForm(false);
    } catch (err) {
      console.error("Update Activity Error:", err);
    }
  };

  // Reload dashboard
  const refreshDashboard = async () => {
    setError("");

    await Promise.all([
      fetchStats(),
      fetchSponsors(),
      fetchPipelineStats(),
      fetchIndustryCounts(),
      fetchUpcomingFollowUps(),
    ]);
  };

  useEffect(() => {
    const loadDashboard = async () => {
      setLoading(true);
      await refreshDashboard();
      setLoading(false);
    };

    loadDashboard();
  }, []);

  useEffect(() => {
    setCurrentPage(1);
  }, [companySearch, statusFilter, industryFilter]);

  // Handle form input
  const handleInputChange = (e) => {
    const { name, value } = e.target;

    setNewSponsor((previous) => ({
      ...previous,
      [name]: value,
    }));
  };

  // Create sponsor
  const handleCreateSponsor = async (e) => {
    e.preventDefault();

    setFormLoading(true);
    setError("");

    try {
      if (editingSponsor) {
        // UPDATE existing sponsor
        await sponsorService.updateSponsor(editingSponsor.id, newSponsor);
      } else {
        // CREATE new sponsor
        await sponsorService.createSponsor(newSponsor);
      }

      // Reset form
      setNewSponsor({
        companyName: "",
        contactPerson: "",
        designation: "",
        email: "",
        phone: "",
        industry: "",
        notes: "",
        status: "LEAD",
      });

      setEditingSponsor(null);
      setShowForm(false);

      // Reload table + statistics
      await refreshDashboard();
    } catch (err) {
      console.error("Save Sponsor API Error:", err);

      setError(err.response?.data?.message || "Failed to save sponsor.");
    } finally {
      setFormLoading(false);
    }
  };
  // Filter sponsors
  const filteredSponsors = sponsors.filter((sponsor) => {
    const matchesCompany = sponsor.companyName
      ?.toLowerCase()
      .includes(companySearch.toLowerCase());

    const matchesStatus =
      statusFilter === "" || sponsor.status === statusFilter;

    const matchesIndustry =
      industryFilter === "" || sponsor.industry === industryFilter;

    return matchesCompany && matchesStatus && matchesIndustry;
  });

  // Get unique industries
  const industries = [
    ...new Set(sponsors.map((sponsor) => sponsor.industry).filter(Boolean)),
  ];

  const sortedSponsors = [...filteredSponsors].sort((a, b) => {
    if (!sortField) {
      return 0;
    }

    const valueA = a[sortField] ?? "";
    const valueB = b[sortField] ?? "";

    const comparison = String(valueA).localeCompare(String(valueB), undefined, {
      numeric: true,
      sensitivity: "base",
    });

    return sortDirection === "asc" ? comparison : -comparison;
  });

  const totalPages = Math.ceil(sortedSponsors.length / sponsorsPerPage);
  const startIndex = (currentPage - 1) * sponsorsPerPage;

  const paginatedSponsors = sortedSponsors.slice(
    startIndex,
    startIndex + sponsorsPerPage,
  );

  const handleDelete = async (sponsor) => {
    const confirmed = window.confirm(
      `Are you sure you want to delete ${sponsor.companyName}?`,
    );

    if (!confirmed) {
      return;
    }

    try {
      setError("");

      await sponsorService.deleteSponsor(sponsor.id);

      await refreshDashboard();
    } catch (err) {
      console.error("Delete Sponsor API Error:", err);

      setError(err.response?.data?.message || "Failed to delete sponsor.");
    }
  };

  const handleSort = (field) => {
    if (sortField === field) {
      setSortDirection(sortDirection === "asc" ? "desc" : "asc");
    } else {
      setSortField(field);
      setSortDirection("asc");
    }
  };

  const clearFilters = () => {
    setCompanySearch("");
    setStatusFilter("");
    setIndustryFilter("");
  };

  const handleView = (sponsor) => {
    setViewingSponsor(sponsor);

    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
  };

  const handleEdit = (sponsor) => {
    setEditingSponsor({
      id: sponsor.id,
      companyName: sponsor.companyName || "",
      contactPerson: sponsor.contactPerson || "",
      designation: sponsor.designation || "",
      email: sponsor.email || "",
      phone: sponsor.phone || "",
      industry: sponsor.industry || "",
      notes: sponsor.notes || "",
      status: sponsor.status || "LEAD",
    });

    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
    setShowForm(true);

    setNewSponsor({
      companyName: sponsor.companyName || "",
      contactPerson: sponsor.contactPerson || "",
      designation: sponsor.designation || "",
      email: sponsor.email || "",
      phone: sponsor.phone || "",
      industry: sponsor.industry || "",
      status: sponsor.status || "LEAD",
      notes: sponsor.notes || "",
    });
  };

  if (loading) {
    return (
      <div className="app">
        <h1>SponsorFlow CRM Dashboard</h1>
        <p>Loading...</p>
      </div>
    );
  }
  const pipelineChartData = [
    {
      stage: "Lead",
      count: pipelineStats.LEAD || 0,
    },
    {
      stage: "Contacted",
      count: pipelineStats.CONTACTED || 0,
    },
    {
      stage: "Meeting",
      count: pipelineStats.MEETING || 0,
    },
    {
      stage: "Proposal",
      count: pipelineStats.PROPOSAL || 0,
    },
    {
      stage: "Won",
      count: pipelineStats.WON || 0,
    },
    {
      stage: "Lost",
      count: pipelineStats.LOST || 0,
    },
  ];

  const industryChartData = Object.entries(industryCounts).map(
    ([industry, count]) => ({
      industry,
      count,
    }),
  );

  const kanbanColumns = [
    {
      status: "LEAD",
      title: "Lead",
    },
    {
      status: "CONTACTED",
      title: "Contacted",
    },
    {
      status: "MEETING",
      title: "Meeting",
    },
    {
      status: "PROPOSAL",
      title: "Proposal",
    },
    {
      status: "WON",
      title: "Won",
    },
    {
      status: "LOST",
      title: "Lost",
    },
  ];

  const formatFollowUpDate = (date) => {
    return new Date(date).toLocaleDateString("en-GB", {
      day: "2-digit",
      month: "short",
      year: "numeric",
    });
  };

  const getFollowUpTiming = (date) => {
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const followUpDate = new Date(date);
    followUpDate.setHours(0, 0, 0, 0);

    const difference = Math.round(
      (followUpDate - today) / (1000 * 60 * 60 * 24),
    );

    if (difference < 0) {
      return `Overdue by ${Math.abs(difference)} day${
        Math.abs(difference) === 1 ? "" : "s"
      }`;
    }

    if (difference === 0) {
      return "Today";
    }

    if (difference === 1) {
      return "Tomorrow";
    }

    return `In ${difference} days`;
  };

  return (
    <div className="app">
      {/* Header */}
      <div className="dashboard-header">
        <div>
          <h1>SponsorFlow CRM Dashboard</h1>
          <p>Manage your sponsorship pipeline</p>
        </div>

        <button
          type="button"
          className="add-sponsor-button"
          onClick={() => {
            setEditingSponsor(null);
            setShowForm(true);

            setNewSponsor({
              companyName: "",
              contactPerson: "",
              designation: "",
              email: "",
              phone: "",
              industry: "",
              notes: "",
              status: "LEAD",
            });
          }}
        >
          + Add Sponsor
        </button>
      </div>

      {/* Error */}
      {error && <p className="error">{error}</p>}

      {/* Dashboard Statistics */}
      {stats && (
        <div className="stats-grid">
          <div className="stat-card">
            <h3>Total Sponsors</h3>
            <p>{stats.totalSponsors}</p>
          </div>

          <div className="stat-card">
            <h3>Lead Sponsors</h3>
            <p>{stats.leadSponsors}</p>
          </div>

          <div className="stat-card">
            <h3>Meeting Sponsors</h3>
            <p>{stats.meetingSponsors}</p>
          </div>

          <div className="stat-card">
            <h3>Won Sponsors</h3>
            <p>{stats.wonSponsors}</p>
          </div>

          <div className="stat-card">
            <h3>Lost Sponsors</h3>
            <p>{stats.lostSponsors}</p>
          </div>
        </div>
      )}
      {/* Dashboard Charts */}
      <div className="charts-grid">
        {/* Pipeline Chart */}
        <div className="chart-card">
          <h2>Sponsorship Pipeline</h2>

          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={pipelineChartData}>
              <CartesianGrid strokeDasharray="3 3" />

              <XAxis dataKey="stage" />

              <YAxis allowDecimals={false} />

              <Tooltip />

              <Bar
                dataKey="count"
                name="Sponsors"
                fill="#172f57"
                radius={[6, 6, 0, 0]}
              >
                <LabelList dataKey="count" position="top" />
              </Bar>
            </BarChart>
          </ResponsiveContainer>
        </div>

        {/* Industry Distribution */}
        <div className="chart-card">
          <h2>Sponsors by Industry</h2>

          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={industryChartData}>
              <CartesianGrid strokeDasharray="3 3" />

              <XAxis dataKey="industry" />

              <YAxis allowDecimals={false} />

              <Tooltip />

              <Bar
                dataKey="count"
                name="Sponsors"
                fill="#172f57"
                radius={[6, 6, 0, 0]}
              >
                <LabelList dataKey="count" position="top" />
              </Bar>
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      <div className="follow-ups-card">
        <div className="follow-ups-header">
          <div>
            <h2>Upcoming Follow-ups</h2>
            <p>Stay on top of your sponsor interactions</p>
          </div>

          <span className="follow-ups-count">{upcomingFollowUps.length}</span>
        </div>

        {upcomingFollowUps.length === 0 ? (
          <div className="follow-ups-empty">No upcoming follow-ups.</div>
        ) : (
          <div className="follow-ups-list">
            {upcomingFollowUps.map((activity) => (
              <div
                className={`follow-up-item ${
                  isFollowUpOverdue(activity.nextFollowUpDate)
                    ? "follow-up-overdue"
                    : ""
                }`}
                key={activity.id}
                onClick={() => {
                  const sponsor = sponsors.find(
                    (sponsor) => sponsor.id === activity.sponsorId,
                  );

                  if (sponsor) {
                    setSelectedSponsor(sponsor);
                    fetchActivities(sponsor.id);
                  }
                }}
              >
                <div className="follow-up-date">
                  <strong>
                    {formatFollowUpDate(activity.nextFollowUpDate)}
                  </strong>
                  {isFollowUpOverdue(activity.nextFollowUpDate) && (
                    <span className="follow-up-overdue-label">OVERDUE</span>
                  )}
                  <span className="follow-up-timing">
                    {getFollowUpTiming(activity.nextFollowUpDate)}
                  </span>
                </div>

                <div className="follow-up-details">
                  <strong>{activity.type.replace("_", " ")}</strong>

                  <p>{activity.description}</p>

                  <small>{activity.sponsorCompanyName}</small>
           
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Sponsor Pipeline Board */}
      <div className="kanban-section">
        <div className="kanban-header">
          <h2>Sponsor Pipeline</h2>
          <p>Track sponsors through the sales process</p>
        </div>

        <div className="kanban-board">
          {kanbanColumns.map((column) => {
            const columnSponsors = sponsors.filter(
              (sponsor) => sponsor.status === column.status,
            );

            return (
              <div
                className="kanban-column"
                key={column.status}
                onDragOver={(event) => event.preventDefault()}
                onDrop={async () => {
                  if (!draggedSponsor) {
                    return;
                  }

                  if (draggedSponsor.status === column.status) {
                    setDraggedSponsor(null);
                    return;
                  }

                  try {
                    const updatedSponsor = {
                      companyName: draggedSponsor.companyName,
                      contactPerson: draggedSponsor.contactPerson,
                      designation: draggedSponsor.designation,
                      email: draggedSponsor.email,
                      phone: draggedSponsor.phone,
                      industry: draggedSponsor.industry,
                      notes: draggedSponsor.notes,
                      status: column.status,
                    };

                    await sponsorService.updateSponsor(
                      draggedSponsor.id,
                      updatedSponsor,
                    );

                    setDraggedSponsor(null);

                    await refreshDashboard();
                  } catch (err) {
                    console.error("Failed to update sponsor status:", err);

                    setError("Failed to update sponsor status.");

                    setDraggedSponsor(null);
                  }
                }}
              >
                <div className="kanban-column-header">
                  <h3>{column.title}</h3>

                  <span>{columnSponsors.length}</span>
                </div>

                <div className="kanban-cards">
                  {columnSponsors.map((sponsor) => (
                    <div
                      className="kanban-card"
                      key={sponsor.id}
                      draggable={true}
                      onDragStart={() => setDraggedSponsor(sponsor)}
                    >
                      <h4>{sponsor.companyName}</h4>

                      <p>{sponsor.contactPerson}</p>

                      <small>{sponsor.industry}</small>
                    </div>
                  ))}
                </div>
              </div>
            );
          })}
        </div>
      </div>

      {/* Add Sponsor Form */}
      {showForm && (
        <section className="form-section">
          <div className="form-header">
            <h2>{editingSponsor ? "Edit Sponsor" : "Add New Sponsor"}</h2>

            <button
              type="button"
              className="close-button"
              onClick={() => {
                setShowForm(false);
                setEditingSponsor(null);

                setNewSponsor({
                  companyName: "",
                  contactPerson: "",
                  designation: "",
                  email: "",
                  phone: "",
                  industry: "",
                  notes: "",
                  status: "LEAD",
                });
              }}
            >
              ×
            </button>
          </div>

          <form onSubmit={handleCreateSponsor}>
            <div className="form-grid">
              <div className="form-group">
                <label>Company Name *</label>
                <input
                  type="text"
                  name="companyName"
                  value={newSponsor.companyName}
                  onChange={handleInputChange}
                  required
                />
              </div>

              <div className="form-group">
                <label>Contact Person *</label>
                <input
                  type="text"
                  name="contactPerson"
                  value={newSponsor.contactPerson}
                  onChange={handleInputChange}
                  required
                />
              </div>

              <div className="form-group">
                <label>Designation</label>
                <input
                  type="text"
                  name="designation"
                  value={newSponsor.designation}
                  onChange={handleInputChange}
                />
              </div>

              <div className="form-group">
                <label>Email</label>
                <input
                  type="email"
                  name="email"
                  value={newSponsor.email}
                  onChange={handleInputChange}
                />
              </div>

              <div className="form-group">
                <label>Phone</label>
                <input
                  type="text"
                  name="phone"
                  value={newSponsor.phone}
                  onChange={handleInputChange}
                />
              </div>

              <div className="form-group">
                <label>Industry *</label>
                <input
                  type="text"
                  name="industry"
                  value={newSponsor.industry}
                  onChange={handleInputChange}
                  required
                />
              </div>

              <div className="form-group">
                <label>Status *</label>
                <select
                  name="status"
                  value={newSponsor.status}
                  onChange={handleInputChange}
                  required
                >
                  <option value="LEAD">LEAD</option>
                  <option value="MEETING">MEETING</option>
                  <option value="WON">WON</option>
                  <option value="LOST">LOST</option>
                </select>
              </div>

              <div className="form-group">
                <label>Notes</label>
                <textarea
                  name="notes"
                  value={newSponsor.notes}
                  onChange={handleInputChange}
                  rows="3"
                />
              </div>
            </div>

            <div className="form-actions">
              <button
                type="button"
                className="cancel-button"
                onClick={() => {
                  setShowForm(false);
                  setEditingSponsor(null);

                  setNewSponsor({
                    companyName: "",
                    contactPerson: "",
                    designation: "",
                    email: "",
                    phone: "",
                    industry: "",
                    notes: "",
                    status: "LEAD",
                  });
                }}
                disabled={formLoading}
              >
                Cancel
              </button>

              <button
                type="submit"
                className="save-button"
                disabled={formLoading}
              >
                {formLoading
                  ? "Saving..."
                  : editingSponsor
                    ? "Update Sponsor"
                    : "Save Sponsor"}
              </button>
            </div>
          </form>
        </section>
      )}

      {/* Sponsor Details */}
      {viewingSponsor && (
        <section className="details-section">
          <div className="details-header">
            <h2>Sponsor Details</h2>

            <button
              type="button"
              className="close-details-button"
              onClick={() => setViewingSponsor(null)}
            >
              Close
            </button>
          </div>

          <div className="details-grid">
            <div className="detail-item">
              <span>Company</span>
              <strong>{viewingSponsor.companyName || "-"}</strong>
            </div>

            <div className="detail-item">
              <span>Contact Person</span>
              <strong>{viewingSponsor.contactPerson || "-"}</strong>
            </div>

            <div className="detail-item">
              <span>Designation</span>
              <strong>{viewingSponsor.designation || "-"}</strong>
            </div>

            <div className="detail-item">
              <span>Email</span>
              {viewingSponsor.email ? (
                <a
                  href={`mailto:${viewingSponsor.email}`}
                  className="detail-link"
                >
                  {viewingSponsor.email}
                </a>
              ) : (
                <strong>-</strong>
              )}
            </div>

            <div className="detail-item">
              <span>Phone</span>
              {viewingSponsor.phone ? (
                <a href={`tel:${viewingSponsor.phone}`} className="detail-link">
                  {viewingSponsor.phone}
                </a>
              ) : (
                <strong>-</strong>
              )}
            </div>

            <div className="detail-item">
              <span>Industry</span>
              <strong>{viewingSponsor.industry || "-"}</strong>
            </div>

            <div className="detail-item">
              <span>Status</span>
              <strong>{viewingSponsor.status || "-"}</strong>
            </div>

            <div className="detail-item">
              <span>Created At</span>
              <strong>
                {viewingSponsor.createdAt
                  ? new Date(viewingSponsor.createdAt).toLocaleString()
                  : "-"}
              </strong>
            </div>

            <div className="detail-item detail-notes">
              <span>Notes</span>
              <p>{viewingSponsor.notes || "-"}</p>
            </div>
          </div>
        </section>
      )}

      <section className="filters-section">
        <h2>Search Sponsors</h2>

        <div className="filters">
          <input
            type="text"
            placeholder="Search company..."
            value={companySearch}
            onChange={(e) => setCompanySearch(e.target.value)}
          />

          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
          >
            <option value="">All Statuses</option>
            <option value="LEAD">LEAD</option>
            <option value="MEETING">MEETING</option>
            <option value="WON">WON</option>
            <option value="LOST">LOST</option>
          </select>

          <select
            value={industryFilter}
            onChange={(e) => setIndustryFilter(e.target.value)}
          >
            <option value="">All Industries</option>

            {industries.map((industry) => (
              <option key={industry} value={industry}>
                {industry}
              </option>
            ))}
          </select>

          <button type="button" onClick={clearFilters}>
            Clear Filters
          </button>
        </div>
      </section>

      {/* Sponsors Table */}
      <section className="sponsors-section">
        <div className="sponsors-header">
          <h2>Sponsors</h2>

          <span>
            Showing {filteredSponsors.length === 0 ? 0 : startIndex + 1}-
            {Math.min(startIndex + sponsorsPerPage, filteredSponsors.length)} of{" "}
            {filteredSponsors.length}
          </span>
        </div>

        {filteredSponsors.length === 0 ? (
          <div className="empty-state">
            <p>No sponsors found.</p>
          </div>
        ) : (
          <div className="table-container">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th
                    onClick={() => handleSort("companyName")}
                    className="sortable-header"
                  >
                    Company
                    {sortField === "companyName" &&
                      (sortDirection === "asc" ? " ↑" : " ↓")}
                  </th>
                  <th
                    onClick={() => handleSort("contactPerson")}
                    className="sortable-header"
                  >
                    Contact
                    {sortField === "contactPerson" &&
                      (sortDirection === "asc" ? " ↑" : " ↓")}
                  </th>
                  <th
                    onClick={() => handleSort("designation")}
                    className="sortable-header"
                  >
                    Designation
                    {sortField === "designation" &&
                      (sortDirection === "asc" ? " ↑" : " ↓")}
                  </th>
                  <th
                    onClick={() => handleSort("industry")}
                    className="sortable-header"
                  >
                    Industry
                    {sortField === "industry" &&
                      (sortDirection === "asc" ? " ↑" : " ↓")}
                  </th>
                  <th
                    onClick={() => handleSort("status")}
                    className="sortable-header"
                  >
                    Status
                    {sortField === "status" &&
                      (sortDirection === "asc" ? " ↑" : " ↓")}
                  </th>
                  <th>Actions</th>
                </tr>
              </thead>

              <tbody>
                {paginatedSponsors.map((sponsor) => (
                  <tr key={sponsor.id}>
                    <td>{sponsor.id}</td>

                    <td>
                      <strong>{sponsor.companyName}</strong>
                    </td>

                    <td>{sponsor.contactPerson}</td>

                    <td>{sponsor.designation}</td>

                    <td>{sponsor.industry}</td>

                    <td>
                      <span
                        className={`status ${sponsor.status?.toLowerCase()}`}
                      >
                        {sponsor.status}
                      </span>
                    </td>

                    <td>
                      <div>
                        <button
                          type="button"
                          className="view-button"
                          onClick={() => {
                            setSelectedSponsor(sponsor);
                            fetchActivities(sponsor.id);
                          }}
                        >
                          View
                        </button>
                        <button
                          type="button"
                          className="edit-button"
                          onClick={() => handleEdit(sponsor)}
                        >
                          Edit
                        </button>
                        <button
                          type="button"
                          className="delete-button"
                          onClick={() => handleDelete(sponsor.id)}
                        >
                          Delete
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>
      {/* Pagination */}
      {totalPages > 1 && (
        <div className="pagination">
          <button
            type="button"
            onClick={() => setCurrentPage((page) => page - 1)}
            disabled={currentPage === 1}
          >
            ← Previous
          </button>

          <span>
            Page {currentPage} of {totalPages}
          </span>

          <button
            type="button"
            onClick={() => setCurrentPage((page) => page + 1)}
            disabled={currentPage === totalPages}
          >
            Next →
          </button>
        </div>
      )}
      {/* Sponsor Details Modal */}
      {selectedSponsor && (
        <div className="modal-overlay">
          <div className="sponsor-modal">
            <div className="modal-header">
              <div>
                <h2>{selectedSponsor.companyName}</h2>
                <p>{selectedSponsor.industry}</p>
              </div>

              <button
                type="button"
                className="modal-close"
                onClick={() => setSelectedSponsor(null)}
              >
                ×
              </button>
            </div>

            <div className="modal-body">
              <div className="sponsor-detail-section">
                <h3>Contact Information</h3>

                <div className="detail-grid">
                  <div>
                    <span>Contact Person</span>
                    <strong>{selectedSponsor.contactPerson || "-"}</strong>
                  </div>

                  <div>
                    <span>Designation</span>
                    <strong>{selectedSponsor.designation || "-"}</strong>
                  </div>

                  <div>
                    <span>Email</span>
                    <strong>{selectedSponsor.email || "-"}</strong>
                  </div>

                  <div>
                    <span>Phone</span>
                    <strong>{selectedSponsor.phone || "-"}</strong>
                  </div>
                </div>
              </div>

              <div className="sponsor-detail-section">
                <h3>Company Information</h3>

                <div className="detail-grid">
                  <div>
                    <span>Industry</span>
                    <strong>{selectedSponsor.industry || "-"}</strong>
                  </div>

                  <div>
                    <span>Status</span>
                    <strong>
                      <span
                        className={`status-badge ${selectedSponsor.status?.toLowerCase()}`}
                      >
                        {selectedSponsor.status || "-"}
                      </span>
                    </strong>
                  </div>
                </div>
              </div>

              <div className="sponsor-detail-section">
                <h3>Notes</h3>

                <p className="sponsor-notes">
                  {selectedSponsor.notes || "No notes available."}
                </p>
              </div>
              <div className="sponsor-detail-section">
                <div className="activity-section-header">
                  <h3>Activity Timeline</h3>

                  <button
                    type="button"
                    className="add-activity-button"
                    onClick={() => setShowActivityForm(true)}
                  >
                    + Add Activity
                  </button>
                </div>

                {showActivityForm && (
                  <div className="activity-form">
                    <div className="activity-form-group">
                      <label>Activity Type</label>

                      <select
                        value={newActivity.type}
                        onChange={(event) =>
                          setNewActivity({
                            ...newActivity,
                            type: event.target.value,
                          })
                        }
                      >
                        <option value="CALL">Call</option>
                        <option value="EMAIL">Email</option>
                        <option value="MEETING">Meeting</option>
                        <option value="FOLLOW_UP">Follow-up</option>
                        <option value="NOTE">Note</option>
                      </select>
                    </div>

                    <div className="activity-form-group">
                      <label>Date</label>

                      <input
                        type="date"
                        value={newActivity.activityDate}
                        onChange={(event) =>
                          setNewActivity({
                            ...newActivity,
                            activityDate: event.target.value,
                          })
                        }
                      />
                    </div>

                    <div className="activity-form-group">
                      <label>Next Follow-up Date</label>

                      <input
                        type="date"
                        value={newActivity.nextFollowUpDate}
                        onChange={(event) =>
                          setNewActivity({
                            ...newActivity,
                            nextFollowUpDate: event.target.value,
                          })
                        }
                      />
                    </div>

                    <div className="activity-form-group">
                      <label>Description</label>

                      <textarea
                        rows="3"
                        placeholder="Describe the activity..."
                        value={newActivity.description}
                        onChange={(event) =>
                          setNewActivity({
                            ...newActivity,
                            description: event.target.value,
                          })
                        }
                      />
                    </div>

                    <div className="activity-form-actions">
                      <button
                        type="button"
                        className="activity-cancel-button"
                        onClick={() => {
                          setShowActivityForm(false);
                          setEditingActivity(null);
                        }}
                      >
                        Cancel
                      </button>

                      <button
                        type="button"
                        className="activity-submit-button"
                        onClick={
                          editingActivity
                            ? handleUpdateActivity
                            : handleCreateActivity
                        }
                      >
                        {editingActivity ? "Update Activity" : "Add Activity"}
                      </button>
                    </div>
                  </div>
                )}

                {activities.length === 0 ? (
                  <p className="activity-empty">No activities recorded yet.</p>
                ) : (
                  <div className="activity-timeline">
                    {activities.map((activity) => (
                      <div className="activity-item" key={activity.id}>
                        <div className="activity-marker">
                          {activity.type === "CALL" && "☎"}
                          {activity.type === "EMAIL" && "✉"}
                          {activity.type === "MEETING" && "●"}
                          {activity.type === "FOLLOW_UP" && "↻"}
                          {activity.type === "NOTE" && "📝"}
                        </div>

                        <div className="activity-content">
                          <div className="activity-top">
                            <div className="activity-dates">
                              <span>{activity.activityDate}</span>

                              {activity.nextFollowUpDate && (
                                <span className="activity-follow-up-date">
                                  Follow-up: {activity.nextFollowUpDate}
                                </span>
                              )}
                            </div>
                          </div>

                          <p>{activity.description}</p>
                          <button
                            type="button"
                            className="activity-edit-button"
                            onClick={() => {
                              setEditingActivity(activity);

                              setNewActivity({
                                type: activity.type,
                                description: activity.description,
                                activityDate: activity.activityDate,
                                nextFollowUpDate: activity.nextFollowUpDate,
                              });

                              setShowActivityForm(true);
                            }}
                          >
                            Edit
                          </button>
                          <button
                            type="button"
                            className="activity-delete-button"
                            onClick={() => handleDeleteActivity(activity.id)}
                          >
                            Delete
                          </button>
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </div>
            </div>

            <div className="modal-footer">
              <button
                type="button"
                className="modal-secondary-button"
                onClick={() => setSelectedSponsor(null)}
              >
                Close
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default App;
