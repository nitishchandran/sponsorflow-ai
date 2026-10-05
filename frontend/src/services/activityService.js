import axios from "axios";

const API_BASE_URL = `${import.meta.env.VITE_API_BASE_URL}/activities`;

const activityService = {
  getActivitiesBySponsor: async (sponsorId) => {
    const response = await axios.get(`${API_BASE_URL}/sponsor/${sponsorId}`);

    return response.data;
  },

  getUpcomingFollowUps: async () => {
    const response = await axios.get(`${API_BASE_URL}/follow-ups`);

    return response.data;
  },

  createActivity: async (activity) => {
    const response = await axios.post(API_BASE_URL, activity);

    return response.data;
  },
  updateActivity: async (activityId, activity) => {
    const response = await axios.put(`${API_BASE_URL}/${activityId}`, activity);

    return response.data;
  },
  deleteActivity: async (activityId) => {
    await axios.delete(`${API_BASE_URL}/${activityId}`);
  },
};

export default activityService;
