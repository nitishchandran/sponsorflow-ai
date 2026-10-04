import axios from "axios";

const API_BASE_URL = "http://localhost:8081/api/sponsors";

const sponsorService = {
    // Get all sponsors
    getAllSponsors: async () => {
        const response = await axios.get(API_BASE_URL);
        return response.data;
    },

    // Get dashboard statistics
    getDashboardStats: async () => {
        const response = await axios.get(
            `${API_BASE_URL}/dashboard/stats`
        );
        return response.data;
    },

    // Get a single sponsor
    getSponsorById: async (id) => {
        const response = await axios.get(
            `${API_BASE_URL}/${id}`
        );
        return response.data;
    },

    // Create a sponsor
    createSponsor: async (sponsor) => {
        const response = await axios.post(
            API_BASE_URL,
            sponsor
        );
        return response.data;
    },

    // Update a sponsor
    updateSponsor: async (id, sponsor) => {
        const response = await axios.put(
            `${API_BASE_URL}/${id}`,
            sponsor
        );
        return response.data;
    },

    // Delete a sponsor
    deleteSponsor: async (id) => {
        await axios.delete(
            `${API_BASE_URL}/${id}`
        );
    },

    // Search sponsors
    searchSponsors: async ({
        company,
        status,
        industry
    }) => {
        const response = await axios.get(
            `${API_BASE_URL}/search`,
            {
                params: {
                    company,
                    status,
                    industry
                }
            }
        );

        return response.data;
    },

    // Get industry counts
    getIndustryCounts: async () => {
        const response = await axios.get(
            `${API_BASE_URL}/dashboard/industry-count`
        );

        return response.data;
    },

    // Get pipeline statistics
    getPipelineStats: async () => {
        const response = await axios.get(
            `${API_BASE_URL}/dashboard/pipeline`
        );

        return response.data;
    }
};

export default sponsorService;